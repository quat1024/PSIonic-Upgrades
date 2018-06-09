package wiresegal.psionup.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wiresegal.psionup.common.command.CommandPsiLearn;
import wiresegal.psionup.common.command.CommandPsiUnlearn;
import wiresegal.psionup.common.core.CommonProxy;
import wiresegal.psionup.common.lib.LibMisc;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES, acceptedMinecraftVersions = LibMisc.VERSIONS)
public class PsionicUpgrades {
	@Mod.Instance
	public static PsionicUpgrades INSTANCE = null;
	
	@SidedProxy(serverSide = LibMisc.PROXY_COMMON, clientSide = LibMisc.PROXY_CLIENT)
	public static CommonProxy PROXY;
	
	public static final Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);
	
	public static void preinit(FMLPreInitializationEvent e) {
		PROXY.pre(e);
	}
	
	public static void init(FMLInitializationEvent e) {
		PROXY.init(e);
	}
	
	public static void postinit(FMLPostInitializationEvent e) {
		PROXY.post(e);
	}
	
	public static void serverStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandPsiLearn());
		e.registerServerCommand(new CommandPsiUnlearn());
	}
}
