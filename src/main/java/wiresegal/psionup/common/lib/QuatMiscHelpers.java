package wiresegal.psionup.common.lib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.ICADColorizer;

public final class QuatMiscHelpers {
	private QuatMiscHelpers() {}
	
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
	
	public static TextComponentTranslation createErrorTranslation(String key, Object... args) {
		TextComponentTranslation txt = new TextComponentTranslation(key, args);
		txt.getStyle().setColor(TextFormatting.RED);
		return txt;
	}
}
