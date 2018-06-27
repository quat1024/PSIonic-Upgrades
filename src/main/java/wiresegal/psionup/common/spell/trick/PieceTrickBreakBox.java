package wiresegal.psionup.common.spell.trick;

import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import wiresegal.psionup.api.BlockProperties;
import wiresegal.psionup.api.ParamBlockProperties;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceTrickBreakBox extends PieceTrick {
	public PieceTrickBreakBox(Spell spell) {
		super(spell);
	}
	
	private SpellParam minParam;
	private SpellParam maxParam;
	private SpellParam totalParam;
	private SpellParam maskParam;
	
	@Override
	public void initParams() {
		minParam = new ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.BLUE, false, false);
		maxParam = new ParamVector(SpellParam.GENERIC_NAME_VECTOR2, SpellParam.RED, false, false);
		totalParam = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.GREEN, false, true);
		maskParam = new ParamBlockProperties(ParamBlockProperties.GENERIC_NAME_MASK, SpellParam.CYAN, true);
		SpellHelpers.Building.addAllParams(this, minParam, maxParam, totalParam, maskParam);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		Double maxBlocksValue = SpellHelpers.Compilation.ensurePositiveAndNonzero(this, totalParam);
		
		meta.addStat(EnumSpellStat.POTENCY, maxBlocksValue.intValue() * 8);
		meta.addStat(EnumSpellStat.COST, maxBlocksValue.intValue() * 8);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos minPos = SpellHelpers.Runtime.getBlockPosFromVectorParam(this, context, minParam);
		BlockPos maxPos = SpellHelpers.Runtime.getBlockPosFromVectorParam(this, context, maxParam);
		Double totalOperations = SpellHelpers.Runtime.getNumber(this, context, totalParam, 0);
		BlockProperties blockProperties = getParamValue(context, maskParam);
		
		if(totalOperations == 0) return null;
		
		int successfulOperations = 0;
		
		for(BlockPos pos : BlockPos.getAllInBoxMutable(minPos, maxPos)) {
			if(!context.isInRadius(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5)) {
				throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
			}
			if(successfulOperations > totalOperations) break;
			
			if(blockProperties != null) {
				if(context.caster.world.getBlockState(pos) != blockProperties.getState()) continue;
			}
			
			SpellHelpers.Runtime.breakBlock(context, pos, pos == minPos || pos == maxPos);
			if(context.caster.world.isAirBlock(pos)) successfulOperations++;
		}
		
		return null;
	}
}
