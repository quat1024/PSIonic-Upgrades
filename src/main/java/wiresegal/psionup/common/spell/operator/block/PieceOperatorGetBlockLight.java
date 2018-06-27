package wiresegal.psionup.common.spell.operator.block;

import vazkii.psi.api.spell.*;
import wiresegal.psionup.api.BlockProperties;

public class PieceOperatorGetBlockLight extends BasePieceOperatorBlockProperties<Double> {
	public PieceOperatorGetBlockLight(Spell spell) {
		super(spell);
	}
	
	@Override
	Double getData(SpellContext context, BlockProperties props) throws SpellRuntimeException {
		return (double) props.getLight();
	}
	
	@Override
	public Class<Double> getEvaluationType() {
		return Double.class;
	}
}
