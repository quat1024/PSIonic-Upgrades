package wiresegal.psionup.common.effect.botania.brew;

import net.minecraft.potion.PotionEffect;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import wiresegal.psionup.common.core.ConfigHandler;
import wiresegal.psionup.common.effect.ModPotions;
import wiresegal.psionup.common.lib.LibNames;

import javax.annotation.Nullable;

public class ModBrews {
	public static Brew psishock;
	@Nullable
	public static Brew psipulse; //Null if potion disabled in config.
	
	public static void init() {
		psishock = new BrewMod(LibNames.Potions.PSISHOCK, 16000, new PotionEffect(ModPotions.psishock, 300));
		psishock.setNotBloodPendantInfusable();
		BotaniaAPI.registerBrew(psishock);
		
		if(ConfigHandler.enablePsionicPulse) {
			psipulse = new BrewMod(LibNames.Potions.PSIPULSE, 32000, new PotionEffect(ModPotions.psipulse, 600));
			psipulse.setNotBloodPendantInfusable();
			psipulse.setNotIncenseInfusable();
			BotaniaAPI.registerBrew(psipulse);
		}
	}
}
