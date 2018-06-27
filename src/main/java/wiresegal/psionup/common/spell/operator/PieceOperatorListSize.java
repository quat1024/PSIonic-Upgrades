package wiresegal.psionup.common.spell.operator;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorListSize extends PieceOperator {
	public PieceOperatorListSize(Spell spell) {
		super(spell);
	}
	
	private SpellParam listParam;
	
	@Override
	public void initParams() {
		listParam = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false);
		addParam(listParam);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		EntityListWrapper listWrapper = getParamValue(context, listParam);
		return listWrapper.unwrap().size();
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
