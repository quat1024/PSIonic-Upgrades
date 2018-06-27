package wiresegal.psionup.common.effect;

import net.minecraft.potion.Potion;

public class ModPotions {
	public static Potion psishock;
	public static Potion psipulse;
	
	//TODO "potion type" whatever that is?
	
	public static void init() {
		psishock = new PotionPsishock();
		psipulse = new PotionPsipulse();
	}
}
