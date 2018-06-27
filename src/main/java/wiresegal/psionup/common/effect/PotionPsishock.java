package wiresegal.psionup.common.effect;

import wiresegal.psionup.common.effect.base.PotionPsiChange;

public class PotionPsishock extends PotionPsiChange {
	public PotionPsishock() {
		super(true, 0xFF4D12);
	}
	
	@Override
	protected int getBaseChangeAmount() {
		return -30;
	}
	
	@Override
	protected int getAmplifierAmount() {
		return -15;
	}
}
