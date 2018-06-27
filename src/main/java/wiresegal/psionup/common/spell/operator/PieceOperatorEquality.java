package wiresegal.psionup.common.spell.operator;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.piece.PieceOperator;
import wiresegal.psionup.common.lib.LibNames;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceOperatorEquality extends PieceOperator {
	public PieceOperatorEquality(Spell spell) {
		super(spell);
	}
	
	private SpellParam firstParam;
	private SpellParam secondParam;
	
	@Override
	public void initParams() {
		firstParam = new ParamAny(LibNames.SpellParams.TARGET_1, SpellParam.RED, false);
		secondParam = new ParamAny(LibNames.SpellParams.TARGET_2, SpellParam.BLUE, false);
		SpellHelpers.Building.addAllParams(this, firstParam, secondParam);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Object first = getParamValue(context, firstParam);
		Object second = getParamValue(context, secondParam);
		
		if(first == null && second == null) return 1d;
		else return first.equals(second) ? 1d : 0d;
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Double.class;
	}
}
