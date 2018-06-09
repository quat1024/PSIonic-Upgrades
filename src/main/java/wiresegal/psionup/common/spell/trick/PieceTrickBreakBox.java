package wiresegal.psionup.common.spell.trick;

import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;
import wiresegal.psionup.api.BlockProperties;
import wiresegal.psionup.api.ParamBlockProperties;
import wiresegal.psionup.common.lib.LibMisc;
import wiresegal.psionup.common.spell.SpellHelpers;

public class PieceTrickBreakBox extends PieceTrick {
	public PieceTrickBreakBox(Spell spell) {
		super(spell);
	}
	
	private SpellParam min;
	private SpellParam max;
	private SpellParam total;
	private SpellParam mask;
	
	@Override
	public void initParams() {
		min = new ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.BLUE, false, false);
		max = new ParamVector(SpellParam.GENERIC_NAME_VECTOR2, SpellParam.RED, false, false);
		total = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.GREEN, false, true);
		//TODO: shove this into libnames
		mask = new ParamBlockProperties(LibMisc.MOD_ID + ".spellparam.mask", SpellParam.CYAN, true);
		SpellHelpers.Building.addAllParams(this, min, max, total, mask);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		Double maxBlocksValue = SpellHelpers.Compilation.ensurePositiveAndNonzero(this, total);
		
		meta.addStat(EnumSpellStat.POTENCY, maxBlocksValue.intValue() * 8);
		meta.addStat(EnumSpellStat.COST, maxBlocksValue.intValue() * 8);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos minPos = SpellHelpers.Runtime.getBlockPosFromVectorParam(this, context, min);
		BlockPos maxPos = SpellHelpers.Runtime.getBlockPosFromVectorParam(this, context, max);
		Double totalOperations = SpellHelpers.Runtime.getNumber(this, context, total, 0);
		BlockProperties blockProperties = getParamValue(context, mask);
		
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
