package wiresegal.psionup.common.spell.operator.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import wiresegal.psionup.api.BlockProperties;

public class PieceOperatorGetBlockProperties extends PieceOperator {
	public PieceOperatorGetBlockProperties(Spell spell) {
		super(spell);
	}
	
	private SpellParam positionParam;
	
	@Override
	public void initParams() {
		positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.YELLOW, false, false);
		addParam(positionParam);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVec = getParamValue(context, positionParam);
		BlockPos position = new BlockPos(positionVec.toVec3D());
		World world = context.focalPoint.world;
		if(!context.isInRadius(positionVec)) {
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
		}
		
		return new BlockProperties(world.getBlockState(position), position, world);
	}
	
	@Override
	public Class<?> getEvaluationType() {
		return BlockProperties.class;
	}
}
