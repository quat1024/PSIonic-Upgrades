package wiresegal.psionup.common.spell.operator;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import wiresegal.psionup.common.lib.LibNames;

public class PieceOperatorPlanarNorm extends PieceOperator {
	public PieceOperatorPlanarNorm(Spell spell) {
		super(spell);
	}
	
	private SpellParam vectorParam;
	
	@Override
	public void initParams() {
		vectorParam = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false);
		addParam(vectorParam);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 vec = getParamValue(context, vectorParam);
		if(!vec.isAxial()) {
			throw new SpellRuntimeException(LibNames.SpellErrors.NON_AXIAL);
		} else return new Vector3(-vec.y, vec.x + vec.z, 0.0).normalize();
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}
}
