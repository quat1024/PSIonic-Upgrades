package wiresegal.psionup.common.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;
import wiresegal.psionup.common.lib.LibMisc;

public class PsionicSoundEvents {
	public static final SoundEvent THWACK = makeSoundEvent("thwack");
	
	//shamelessly stolen from botania \:D/
	private static SoundEvent makeSoundEvent(String name) {
		ResourceLocation loc = new ResourceLocation(LibMisc.MOD_ID, name);
		return new SoundEvent(loc).setRegistryName(loc);
	}
	
	public static void registerSounds(IForgeRegistry<SoundEvent> reg) {
		reg.register(THWACK);
	}
}
