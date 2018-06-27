package wiresegal.psionup.common.spell.trick;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import wiresegal.psionup.api.BlockProperties;
import wiresegal.psionup.api.ParamBlockProperties;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceTrickCloneBox extends PieceTrick {
	public PieceTrickCloneBox(Spell spell) {
		super(spell);
	}
	
	private SpellParam minParam;
	private SpellParam maxParam;
	private SpellParam baseParam;
	private SpellParam maskParam;
	
	@Override
	public void initParams() {
		minParam = new ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.BLUE, false, false);
		maxParam = new ParamVector(SpellParam.GENERIC_NAME_VECTOR2, SpellParam.RED, false, false);
		baseParam = new ParamVector(SpellParam.GENERIC_NAME_BASE, SpellParam.GREEN, false, false);
		maskParam = new ParamBlockProperties(ParamBlockProperties.GENERIC_NAME_MASK, SpellParam.CYAN, false);
		
		SpellHelpers.Building.addAllParams(this, minParam, maxParam, baseParam, maskParam);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		meta.addStat(EnumSpellStat.POTENCY, 200);
		meta.addStat(EnumSpellStat.COST, 1800);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		BlockPos minSpecifiedPos = SpellHelpers.Runtime.getBlockPosFromVectorParam(this, context, minParam);
		BlockPos maxSpecifiedPos = SpellHelpers.Runtime.getBlockPosFromVectorParam(this, context, maxParam);
		BlockPos basePos = SpellHelpers.Runtime.getBlockPosFromVectorParam(this, context, baseParam);
		BlockProperties blockProperties = getParamValue(context, maskParam);
		
		//untangle the user's specified min and max, if they get mixed up
		BlockPos minPos = new BlockPos(Math.min(minSpecifiedPos.getX(), maxSpecifiedPos.getX()), Math.min(minSpecifiedPos.getY(), maxSpecifiedPos.getY()), Math.min(minSpecifiedPos.getZ(), maxSpecifiedPos.getZ()));
		
		int xWidth = Math.abs(minSpecifiedPos.getX() - maxSpecifiedPos.getX());
		int height = Math.abs(minSpecifiedPos.getY() - maxSpecifiedPos.getY());
		int zWidth = Math.abs(minSpecifiedPos.getZ() - maxSpecifiedPos.getZ());
		
		BlockPos.MutableBlockPos mutTestPos = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mutPlacePos = new BlockPos.MutableBlockPos();
		
		for(int x = 0; x < xWidth; x++) {
			for(int y = 0; y < height; y++) {
				for(int z = 0; z < zWidth; z++) {
					mutTestPos.setPos(minPos.getX() + x, minPos.getY() + y, minPos.getZ() + z);
					mutPlacePos.setPos(basePos.getX() + x, basePos.getY() + y, basePos.getZ() + z);
					
					if(!SpellHelpers.Runtime.isBlockPosInRadius(context, mutTestPos) || !SpellHelpers.Runtime.isBlockPosInRadius(context, mutPlacePos)) {
						throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
					}
					
					IBlockState atState = context.caster.world.getBlockState(mutTestPos);
					if(atState == blockProperties.getState()) {
						SpellHelpers.Runtime.placeBlockFromInventory(context, mutPlacePos, mutPlacePos == minSpecifiedPos || mutPlacePos == maxSpecifiedPos);
					}
				}
			}
		}
		
		return null;
	}
}

