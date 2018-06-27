package wiresegal.psionup.common.spell.operator;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import wiresegal.psionup.common.lib.LibNames;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceOperatorVectorRotate extends PieceOperator {
	public PieceOperatorVectorRotate(Spell spell) {
		super(spell);
	}
	
	private SpellParam vectorParam;
	private SpellParam axisParam;
	private SpellParam angleParam;
	
	@Override
	public void initParams() {
		vectorParam = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false);
		axisParam = new ParamVector(LibNames.SpellParams.AXIS, SpellParam.CYAN, false, false);
		angleParam = new ParamNumber(LibNames.SpellParams.ANGLE, SpellParam.GREEN, false, false);
		SpellHelpers.Building.addAllParams(this, vectorParam, axisParam, angleParam);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 vector = getParamValue(context, vectorParam);
		Vector3 axisVector = getParamValue(context, axisParam);
		
		if(vector == null || axisVector == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		
		double angle = SpellHelpers.Runtime.getNumber(this, context, angleParam, 0);
		
		return vector.rotate(angle, axisVector);
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}
}
