package wiresegal.psionup.common.spell.trick;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import wiresegal.psionup.common.lib.LibNames;
import wiresegal.psionup.common.lib.SpellHelpers;
import wiresegal.psionup.common.network.MessageParticleTrail;
import wiresegal.psionup.common.network.PsionicPacketHandler;

public class PieceTrickParticleTrail extends PieceTrick {
	public PieceTrickParticleTrail(Spell spell) {
		super(spell);
	}
	
	private SpellParam positionParam;
	private SpellParam rayParam;
	private SpellParam lengthParam;
	private SpellParam timeParam;
	
	@Override
	public void initParams() {
		positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
		rayParam = new ParamVector(LibNames.SpellParams.VAZKII_RAY, SpellParam.GREEN, false, false);
		lengthParam = new ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.CYAN, false, true);
		timeParam = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false);
		
		SpellHelpers.Building.addAllParams(this, positionParam, rayParam, lengthParam, timeParam);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		
		double length = SpellHelpers.Compilation.ensurePositiveAndNonzero(this, lengthParam);
		meta.addStat(EnumSpellStat.POTENCY, (int) length * 10);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 pos = getParamValue(context, positionParam);
		Vector3 dir = getParamValue(context, rayParam);
		double length = SpellHelpers.Runtime.getNumber(this, context, lengthParam, 0);
		double time = Math.min(SpellHelpers.Runtime.getNumber(this, context, timeParam, 20), 400);
		
		if(time < 0d) throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
		
		if(pos == null || dir == null) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		
		if(!context.isInRadius(pos) || !context.isInRadius(pos.copy().add(dir.copy().multiply(length)))) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}
		
		if(!context.caster.world.isRemote) {
			PsionicPacketHandler.NET.sendToDimension(new MessageParticleTrail(pos.toVec3D(), dir.toVec3D(), length, (int) time, PsiAPI.getPlayerCAD(context.caster)), context.caster.world.provider.getDimension());
		}
		
		return null;
	}
}
