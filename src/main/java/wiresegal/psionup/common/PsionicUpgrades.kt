package wiresegal.psionup.common

import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import wiresegal.psionup.common.command.CommandPsiLearn
import wiresegal.psionup.common.command.CommandPsiUnlearn
import wiresegal.psionup.common.core.CommonProxy
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 8:29 AM on 3/20/16.
 */
@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES)
class PsionicUpgrades {
    companion object {
        @Mod.Instance(LibMisc.MOD_ID)
        lateinit var instance: PsionicUpgrades

        @SidedProxy(serverSide = LibMisc.PROXY_COMMON,
                clientSide = LibMisc.PROXY_CLIENT)
        lateinit var proxy: CommonProxy

        val LOGGER = LogManager.getLogger(LibMisc.MOD_ID)

        val DEV_ENVIRONMENT: Boolean by lazy {
            Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean
        }
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        proxy.pre(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @Mod.EventHandler
    fun post(event: FMLPostInitializationEvent) {
        proxy.post(event)
    }

    @Mod.EventHandler
    fun serverStartingEvent(e: FMLServerStartingEvent) {
        e.registerServerCommand(CommandPsiLearn());
        e.registerServerCommand(CommandPsiUnlearn());
    }
}
