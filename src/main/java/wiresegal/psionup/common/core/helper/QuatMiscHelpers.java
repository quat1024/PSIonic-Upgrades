package wiresegal.psionup.common.core.helper;

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
	
	public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound) {
		emitSoundFromEntity(world, entity, sound, 1f, 1f);
	}
	
	public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound, float volume, float pitch) {
		emitSoundFromEntity(world, entity, sound, SoundCategory.PLAYERS, volume, pitch);
	}
	
	public static void emitSoundFromEntity(World world, EntityLivingBase entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
		world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, category, volume, pitch);
	}
}
