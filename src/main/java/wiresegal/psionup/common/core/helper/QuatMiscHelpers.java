package wiresegal.psionup.common.core.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.ICADColorizer;

public final class QuatMiscHelpers {
	private QuatMiscHelpers() {}
	
	public static float[] splitColor(int color) {
		float red = ((color & 0xFF0000) >> 16) / 255f;
		float green = ((color & 0x00FF00) >> 8) / 255f;
		float blue = (color & 0x0000FF) / 255f;
		
		return new float[]{red, green, blue};
	}
	
	public static final float[] DEFAULT_CAD_COLOR_SPLIT = splitColor(ICADColorizer.DEFAULT_SPELL_COLOR);
	
	@SideOnly(Side.CLIENT)
	public static float[] getSplitColorizerColor(ItemStack stack) {
		if(stack.isEmpty()) return DEFAULT_CAD_COLOR_SPLIT;
		
		else return splitColor(((ICADColorizer)stack.getItem()).getColor(stack));
	}
}
