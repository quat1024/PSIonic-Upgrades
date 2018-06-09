package wiresegal.psionup.common.spell;

import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;
import vazkii.psi.common.spell.trick.block.PieceTrickPlaceBlock;

public class SpellHelpers {	
	public static class Building {
		public static void addAllParams(SpellPiece piece, SpellParam... params) {
			for(SpellParam param : params) {
				piece.addParam(param);
			}
		}
	}
	
	public static class Compilation {
		public static Double ensurePositiveAndNonzero(SpellPiece piece, SpellParam param) throws SpellCompilationException {
			Double val = piece.getParamEvaluation(param);
			
			if(val == null || val <= 0) {
				throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);
			}
			
			return val;
		}
		
		public static Double ensurePositiveOrzero(SpellPiece piece, SpellParam param) throws SpellCompilationException {
			Double val = piece.getParamEvaluation(param);
			
			if(val == null || val <= 0) {
				throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, piece.x, piece.y);
			}
			
			return val;
		}
	}
	
	public static class Runtime {
		public static BlockPos getBlockPosFromVectorParam(SpellPiece piece, SpellContext context, SpellParam param) {
			return ((Vector3) piece.getParamValue(context, param)).toBlockPos();
		}
		
		public static boolean isBlockPosInRadius(SpellContext context, BlockPos pos) {
			return context.isInRadius(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
		}
		
		public static void placeBlock(SpellContext context, BlockPos pos, boolean particles) {
			PieceTrickPlaceBlock.placeBlock(context.caster, context.caster.world, pos, context.targetSlot, particles);
		}
		
		public static void breakBlock(SpellContext context, BlockPos pos, boolean particles) {
			PieceTrickBreakBlock.removeBlockWithDrops(context, context.caster, context.caster.world, context.tool, pos, particles);
		}
	}
}
