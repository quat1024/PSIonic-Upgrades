package wiresegal.psionup.common.gui.magazine;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import vazkii.psi.client.core.handler.ClientTickHandler;
import wiresegal.psionup.common.lib.LibMisc;

public class GuiCADMagazine extends GuiContainer {
	
	private static final ResourceLocation texture = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/magazine.png");
	
	public GuiCADMagazine(EntityPlayer player, ItemStack stack) {
		super(new ContainerCADMagazine(player, stack));
	}
	
	int lastTick = 0;
	
	private String getTooltipText() {
		return ((ContainerCADMagazine)inventorySlots).tooltipText;
	}
	
	private float getTooltipTime() {
		return ((ContainerCADMagazine)inventorySlots).tooltipTime;
	}
	
	private void decreaseTooltipTime(float value) {
		((ContainerCADMagazine)inventorySlots).tooltipTime = getTooltipTime() - value;
	}
	
	@Override
	public void initGui() {
		xSize = 194;
		ySize = 192;
		super.initGui();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		
		GlStateManager.color(1f, 1f, 1f);
		
		mc.getTextureManager().bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		for(Slot slot : inventorySlots.inventorySlots) {
			if(!(slot instanceof ContainerCADMagazine.SlotCustomBullet)) continue;
			if(!slot.isEnabled()) continue;
			
			boolean dark = ((ContainerCADMagazine.SlotCustomBullet) slot).dark;
			drawTexturedModalRect(slot.xPos + x, slot.yPos + y, 16, 224 + (dark ? 16 : 0), 16, 16);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		if(getTooltipTime() > 0) {
			GuiUtils.drawHoveringText(ImmutableList.of(TextFormatting.RED + I18n.format(getTooltipText())), -10, ySize / 2, width, height, xSize, Minecraft.getMinecraft().fontRenderer);
			decreaseTooltipTime(ClientTickHandler.ticksInGame - lastTick);
		}
		lastTick = ClientTickHandler.ticksInGame;
	}
}
