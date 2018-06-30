package wiresegal.psionup.common.effect.botania.brew;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import scala.actors.threadpool.Arrays;
import vazkii.botania.api.brew.Brew;
import wiresegal.psionup.common.lib.LibMisc;

public class BrewMod extends Brew {
	public BrewMod(String name, int color, int cost, PotionEffect... effects) {
		super(name, name, color, cost, effects);
	}
	
	@SuppressWarnings("unchecked")
	public BrewMod(String name, int cost, PotionEffect... effects) {
		this(name, PotionUtils.getPotionColorFromEffectList(Arrays.asList(effects)), cost, effects);
	}
	
	@Override
	public String getUnlocalizedName() {
		return LibMisc.MOD_ID + ".brew." + super.getUnlocalizedName();
	}
}
