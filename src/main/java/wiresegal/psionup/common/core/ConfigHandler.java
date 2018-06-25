package wiresegal.psionup.common.core;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.text.WordUtils;
import wiresegal.psionup.common.lib.LibMisc;

import java.io.File;

@Mod.EventBusSubscriber
public class ConfigHandler {
	public static boolean enableInline;
	public static boolean enableMagazine;
	public static boolean enableCase;
	public static boolean enableRing;
	public static boolean enablePsionicPulse;
	
	private static Configuration config;
	
	static void initConfig(File configFile) {
		config = new Configuration(configFile);
		
		processConfig();
	}
	
	static void processConfig() {
		enableInline = isEnabled("enableInlineCaster", "Is the Inline Caster craftable?", true);
		enableMagazine = isEnabled("enableSpellMagazine", "Is the Spell Magazine craftable?", true);
		enableCase = isEnabled("enableCADCase", "Is the CAD Case craftable?", true);
		enableRing = isEnabled("enableFlashRing", "Is the Flash Ring craftable?", true);
		enablePsionicPulse = isEnabled("enablePsionicPulse", "Is the Psionic Pulse potion brewable?", true);
		
		if(config.hasChanged()) config.save();
	}
	
	private static boolean isEnabled(String configName, String description, boolean def) {
		return config.getBoolean(configName, "general", def, description, LibMisc.MOD_ID + ".config." + configName);
	}
	
	@SubscribeEvent
	public static void changed(ConfigChangedEvent e) {
		if(e.getModID().equals(LibMisc.MOD_ID)) {
			processConfig();
		}
	}
}
