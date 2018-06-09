package wiresegal.psionup.common.spell.trick;

import net.minecraft.block.state.IBlockState;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceTrick;
import wiresegal.psionup.common.block.ModBlocks;
import wiresegal.psionup.common.block.spell.BlockConjuredPulsar;

public class PieceTrickConjurePularLight extends PieceTrickConjurePulsar {
	public PieceTrickConjurePularLight(Spell spell) {
		super(spell);
	}
	
	@Override
	protected void addStats(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 60);
		meta.addStat(EnumSpellStat.COST, 210);
		meta.addStat(EnumSpellStat.COMPLEXITY, 2);
	}
	
	@Override
	public IBlockState getStateToSet() {
		return ModBlocks.conjuredPulsar.getDefaultState().withProperty(BlockConjuredPulsar.SOLID, true).withProperty(BlockConjuredPulsar.LIGHT, true);
	}
}
