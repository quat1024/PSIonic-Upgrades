package wiresegal.psionup.common.core.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
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
	
	public static float[] lerpColor(float[] splitColor1, float[] splitColor2, float lerp) {
		float[] ret = new float[3];
		ret[0] = splitColor1[0] * (1 - lerp) + splitColor2[0] * lerp;
		ret[1] = splitColor1[1] * (1 - lerp) + splitColor2[1] * lerp;
		ret[2] = splitColor1[2] * (1 - lerp) + splitColor2[2] * lerp;
		return ret;
	}
	
	public static int mergeColor(float[] splitColor) {
		int red = (int) (splitColor[0] * 255) & 0xFF;
		int green = (int) (splitColor[1] * 255) & 0xFF;
		int blue = (int) (splitColor[2] * 255) & 0xFF;
		return (red << 16) | (green << 8) | blue;
	}
	
	public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound) {
		emitSoundFromEntity(world, entity, sound, 1f, 1f);
	}
	
	public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound, float volume, float pitch) {
		emitSoundFromEntity(world, entity, sound, SoundCategory.PLAYERS, volume, pitch);
	}
	
	public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
		world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, category, volume, pitch);
	}
	
	public static double distanceSquared(double x1, double y1, double z1, double x2, double y2, double z2) {
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) + (z1 - z2);
	}
	
	public static double distanceSquared(Entity a, Entity b) {
		return distanceSquared(a.posX, a.posY, a.posZ, b.posX, b.posY, b.posZ);
	}
}
