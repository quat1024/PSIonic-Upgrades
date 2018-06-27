package wiresegal.psionup.common.spell.operator;

import net.minecraft.util.math.RayTraceResult;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import wiresegal.psionup.common.lib.LibNames;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceOperatorVectorStrongRaycast extends PieceOperator {
	public PieceOperatorVectorStrongRaycast(Spell spell) {
		super(spell);
	}
	
	private SpellParam originParam;
	private SpellParam rayParam;
	private SpellParam maxParam;
	
	//The maximum value for "max".
	//What a dumb name, yeah, I know.
	private static final double MAX_MAX = 32d;
	
	@Override
	public void initParams() {
		originParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
		rayParam = new ParamVector(LibNames.SpellParams.VAZKII_RAY, SpellParam.GREEN, false, false);
		maxParam = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false);
		SpellHelpers.Building.addAllParams(this, originParam, rayParam, maxParam);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 originVec = getParamValue(context, originParam);
		Vector3 rayVec = getParamValue(context, rayParam);
		
		if(originVec == null || rayVec == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		
		//TODO: The original doesn't protect at all against *negative* lengths.
		//Is that okay?
		double max = SpellHelpers.Runtime.getNumber(this, context, maxParam, MAX_MAX);
		max = Math.min(max, MAX_MAX);
		
		Vector3 endVec = originVec.copy().add(rayVec.copy().normalize().multiply(max));
		RayTraceResult res = context.caster.world.rayTraceBlocks(originVec.toVec3D(), endVec.toVec3D(), false, true, false);
		
		if(res != null && res.getBlockPos() != null) {
			return createResult(res);
		} else throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
	}
	
	protected Vector3 createResult(RayTraceResult res) {
		return Vector3.fromBlockPos(res.getBlockPos());
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}
}
