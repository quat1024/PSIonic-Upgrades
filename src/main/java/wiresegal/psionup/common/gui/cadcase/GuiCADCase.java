package wiresegal.psionup.common.gui.cadcase;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import wiresegal.psionup.common.lib.LibMisc;

public class GuiCADCase extends GuiContainer {
	
	private static final ResourceLocation textureMain = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/case_base.png");
	private static final ResourceLocation textureCase = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/cases.png");
	private static final int xOffset = 72;
	private static final int yOffset = 5 + 29;
	
	public GuiCADCase(EntityPlayer player, ItemStack stack) {
		super(new ContainerCADCase(player, stack));
		
		this.player = player;
		this.stack = stack;
	}
	
	final EntityPlayer player;
	final ItemStack stack;
	
	@Override
	public void initGui() {
		xSize = 227;
		ySize = 130;
		super.initGui();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1f, 1f, 1f);
		
		mc.getTextureManager().bindTexture(textureMain);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y + yOffset, 0, 0, xSize, 96);
		
		//TODO deals with item damage.
		mc.getTextureManager().bindTexture(textureCase);
		int u = (stack.getItemDamage() % 3) * 83;
		int v = (stack.getItemDamage() / 3) * 29;
		drawTexturedModalRect(x + xOffset, y, u, v, 83, 29);
	}
}
