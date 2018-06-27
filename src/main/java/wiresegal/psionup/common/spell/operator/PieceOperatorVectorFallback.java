package wiresegal.psionup.common.spell.operator;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import wiresegal.psionup.common.lib.LibNames;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceOperatorVectorFallback extends PieceOperator {
	public PieceOperatorVectorFallback(Spell spell) {
		super(spell);
	}
	
	private SpellParam vectorParam;
	private SpellParam fallbackParam;
	
	@Override
	public void initParams() {
		vectorParam = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false);
		fallbackParam = new ParamVector(LibNames.SpellParams.FALLBACK, SpellParam.GREEN, false, false);
		SpellHelpers.Building.addAllParams(this, vectorParam, fallbackParam);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 vec = getParamValue(context, vectorParam);
		if(vec == null || vec.isZero()) {
			return getParamValue(context, fallbackParam);
		} else return vec;
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}
}
