package wiresegal.psionup.common.effect;

import com.teamwizardry.librarianlib.features.base.PotionMod;

public class ModPotions {
	//TODO: remove liblib PotionMod
	public static PotionMod psishock;
	public static PotionMod psipulse;
	
	//TODO "potion type" whatever that is?
	
	public static void init() {
		psishock = new PotionPsishock();
		psipulse = new PotionPsipulse();
	}
}
