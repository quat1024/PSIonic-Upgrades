package wiresegal.psionup.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.*;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.client.core.handler.ShaderHandler;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.lib.LibResources;
import wiresegal.psionup.common.items.ItemGaussRifle;
import wiresegal.psionup.common.items.spell.ItemFlashRing;

import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class HUDHandler {
	@SubscribeEvent
	public static void gameOverlayPost(RenderGameOverlayEvent.Post e) {
		if(e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			drawPsiBar(e.getResolution(), e.getPartialTicks());
		}
	}
	
	private static ResourceLocation psiBarLocation = new ResourceLocation(LibResources.GUI_PSI_BAR);
	private static ResourceLocation psiBarMaskLocation = new ResourceLocation(LibResources.GUI_PSI_BAR_MASK);
	private static ResourceLocation psiBarShatterLocation = new ResourceLocation(LibResources.GUI_PSI_BAR_SHATTER);
	
	private static int secondaryTextureUnit = 7;
	private static boolean registeredMask = false;
	
	private static void drawPsiBar(ScaledResolution res, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack playerCad = PsiAPI.getPlayerCAD(mc.player);
		if(playerCad.isEmpty()) return;
		
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(mc.player);
		if(data.level == 0 && !mc.player.capabilities.isCreativeMode) {
			return;
		}
		
		ICAD cad = (ICAD) playerCad.getItem();
		
		int totalPsi = data.getTotalPsi();
		int currentPsi = data.getAvailablePsi();
		
		ItemStack mainStack = mc.player.getHeldItemMainhand();
		ItemStack offStack = mc.player.getHeldItemOffhand();
		
		if(currentPsi == totalPsi) {
			//Return if Psi's regular HUD handler will be in charge of rendering the psi bar.
			//No need to draw twice.
			if(mainStack.isEmpty() || mainStack.getItem() instanceof ISocketable) return;
			if(offStack.isEmpty() || offStack.getItem() instanceof ISocketable) return;
		}
		
		//Should Psionic Upgrades draw its own psi bar?
		if((mainStack.isEmpty() || !(mainStack.getItem() instanceof ItemFlashRing) && !(mainStack.getItem() instanceof ItemGaussRifle)) && (offStack.isEmpty() || !(offStack.getItem() instanceof ItemFlashRing) && !(offStack.getItem() instanceof ItemGaussRifle))) {
			return;
		}
		
		//TODO: Make sure this is in sync with Psi's HUDHandler#drawPsiBar
		//I made this method by translating Kotlin not by adapting Psi's stuff
		
		GlStateManager.pushMatrix();
		
		//Fix the scale factor if it's going to be the wrong size.
		int scaleFactor = res.getScaleFactor();
		if(scaleFactor > ConfigHandler.maxPsiBarScale) {
			int oldGuiScale = mc.gameSettings.guiScale;
			mc.gameSettings.guiScale = ConfigHandler.maxPsiBarScale;
			res = new ScaledResolution(mc);
			mc.gameSettings.guiScale = oldGuiScale;
			
			float f = ConfigHandler.maxPsiBarScale / (float) scaleFactor;
			GlStateManager.scale(f, f, f);
		}
		
		boolean onRight = ConfigHandler.psiBarOnRight;
		
		int padding = 3;
		int width = 32;
		int height = 140;
		
		int x = -padding;
		int y = res.getScaledHeight() / 2 - height / 2;
		if(onRight) {
			x = res.getScaledWidth() + padding - width;
		}
		
		//What
		if(!registeredMask) {
			mc.renderEngine.bindTexture(psiBarMaskLocation);
			mc.renderEngine.bindTexture(psiBarShatterLocation);
			registeredMask = true;
		}
		
		mc.renderEngine.bindTexture(psiBarLocation);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, width, height, 64f, 256f);
		
		x += 8;
		y += 26; //MAGIC NUMBERSSSSSSSSSSSSSS
		
		width = 16;
		height = 106;
		
		float red = 0.6f;
		float green = 0.65f;
		float blue = 1f;
		
		int originalHeight = height;
		int originalY = y;
		double v = 0;
		int max = totalPsi;
		
		int texture = 0;
		boolean shaders = ShaderHandler.useShaders();
		
		if(shaders) {
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		}
		
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		for(PlayerDataHandler.PlayerData.Deduction d : data.deductions) {
			float alpha = d.getPercentile(partialTicks);
			GlStateManager.color(red, green, blue, alpha);
			double effHeight = originalHeight * d.deduct / (double) max; 
			height = MathHelper.ceil(effHeight);
			v = originalHeight - effHeight;
			y = originalY + (int) v; //??????? won't v always be very small???
			
			ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(alpha, d.shatter));
			Gui.drawModalRectWithCustomSizedTexture(x, y, 32f, (float) v, width, height, 64f, 256f);
		}
		
		float textY = originalY;
		if(max > 0) {
			height = MathHelper.floor(originalHeight * data.availablePsi / (double) max);
			v = originalHeight - height;
			y = originalY + (int) v;
			
			if(data.availablePsi != data.lastAvailablePsi) {
				float textHeight = (originalHeight * (data.availablePsi * partialTicks + data.lastAvailablePsi * (1 - partialTicks)) / (float) max);
				textY = originalY + (originalHeight - textHeight);
			} else textY = y;
		} else height = 0;
		
		GlStateManager.color(red, green, blue);
		ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(1f, false));
		Gui.drawModalRectWithCustomSizedTexture(x, y, 32f, (float) v, width, height, 64f, 256f);
		ShaderHandler.releaseShader();
		
		if(shaders) {
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
		}
		
		GlStateManager.color(1f, 1f, 1f);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, textY, 0f);
		width = 44;
		height = 3;
		
		String line1 = String.valueOf(data.availablePsi);
		String line2 = String.valueOf(cad.getStoredPsi(playerCad));
		
		int offBar;
		int offLine1;
		int offLine2;
		
		if(onRight) {
			offBar = 22;
			offLine1 = 7 + mc.fontRenderer.getStringWidth(line1);
			offLine2 = 7 + mc.fontRenderer.getStringWidth(line2);
		} else {
			offBar = 6;
			offLine1 = -23;
			offLine2 = -23;
		}
		
		int spellColor = cad.getSpellColor(playerCad);
		float spellRed = ((spellColor & 0xFF0000) >> 16) / 255f;
		float spellGreen = ((spellColor & 0x00FF00) >> 8) / 255f;
		float spellBlue = (spellColor & 0x0000FF) / 255f;
		GlStateManager.color(spellRed, spellGreen, spellBlue);
		
		Gui.drawModalRectWithCustomSizedTexture(x - offBar, -2, 0f, 140f, width, height, 64f, 256f);
		mc.fontRenderer.drawStringWithShadow(line1, x - offLine1, -11f, 0xFFFFFF);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, Math.max(textY + 3, originalY + 100), 0f);
		mc.fontRenderer.drawStringWithShadow(line2, x = offLine2, 0f, 0xFFFFFF);
		GlStateManager.popMatrix();
		
		GlStateManager.popMatrix();
	}
	
	private static Consumer<Integer> generateCallback(float percentile, boolean shatter) {
		Minecraft mc = Minecraft.getMinecraft();
		return (shader) -> {
			int percentileUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "percentile");
			int imageUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "image");
			int maskUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "mask");
			
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(psiBarLocation).getGlTextureId());
			ARBShaderObjects.glUniform1iARB(imageUniform, 0);
			
			OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit);
			
			GlStateManager.enableTexture2D();
			//GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D); //TODO dangling getter?
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(shatter ? psiBarShatterLocation : psiBarMaskLocation).getGlTextureId());
			ARBShaderObjects.glUniform1iARB(maskUniform, secondaryTextureUnit);
			
			ARBShaderObjects.glUniform1fARB(percentileUniform, percentile);
		};
	}
}