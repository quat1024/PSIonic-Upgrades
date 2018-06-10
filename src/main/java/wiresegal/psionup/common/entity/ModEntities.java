package wiresegal.psionup.common.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;
import wiresegal.psionup.common.lib.LibMisc;
import wiresegal.psionup.common.lib.LibNames;

public class ModEntities {
	public static void register(IForgeRegistry<EntityEntry> reg) {
		int netID = 0;
		
		EntityEntry gauss = EntityEntryBuilder.create()
						.entity(EntityGaussPulse.class)
						.id(new ResourceLocation(LibMisc.MOD_ID, "gauss"), netID++)
						.name(LibNames.Entities.GAUSS_PULSE)
						.tracker(256, 10, true)
						.build();
		
		reg.register(gauss);
	}
	
	public static void createWatchers() {
		EntityGaussPulse.createWatchers();
	}
}
