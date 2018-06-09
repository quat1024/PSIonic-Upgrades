package wiresegal.psionup.common.entity

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityRegistry
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.lib.LibMisc
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 9:44 PM on 7/13/16.
 */
object ModEntitiesKt {
    private var id = 0

    init {
        EntityRegistry.registerModEntity(ResourceLocation(LibMisc.MOD_ID, "gauss"), EntityGaussPulse::class.java, LibNames.Entities.GAUSS_PULSE, id++, PsionicUpgrades.INSTANCE, 256, 10, true)
    }
}
