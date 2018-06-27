package wiresegal.psionup.common.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import vazkii.psi.api.cad.ICADColorizer;
import wiresegal.psionup.common.effect.base.PotionPsiChange;

public class PotionPsipulse extends PotionPsiChange {
	public PotionPsipulse() {
		super(false, ICADColorizer.DEFAULT_SPELL_COLOR);
	}
	
	@Override
	protected int getBaseChangeAmount() {
		return 20;
	}
	
	@Override
	protected int getAmplifierAmount() {
		return 10;
	}
	
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		PotionEffect psishockEffect = entity.getActivePotionEffect(ModPotions.psishock);
		if(psishockEffect != null) {
			PotionEffect thisEffect = entity.getActivePotionEffect(this);
			assert thisEffect != null;
			psishockEffect.combine(new PotionEffect(ModPotions.psishock, psishockEffect.getDuration() + thisEffect.getDuration(), Math.min(psishockEffect.getAmplifier() + thisEffect.getAmplifier() + 1, 127)));
			thisEffect.combine(new PotionEffect(this, 0, amplifier + 1));
		} else {
			super.performEffect(entity, amplifier);
		}
	}
}
