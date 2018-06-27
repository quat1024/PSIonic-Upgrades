package wiresegal.psionup.common.spell.operator.block;

import vazkii.psi.api.spell.*;
import wiresegal.psionup.api.BlockProperties;

public class PieceOperatorGetBlockHardness extends BasePieceOperatorBlockProperties<Double> {
	public PieceOperatorGetBlockHardness(Spell spell) {
		super(spell);
	}
	
	@Override
	Double getData(SpellContext context, BlockProperties props) throws SpellRuntimeException {
		return (double) props.getHardness();
	}
	
	@Override
	public Class<Double> getEvaluationType() {
		return Double.class;
	}
}
