package wiresegal.psionup.common.spell.trick;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import wiresegal.psionup.common.block.ModBlocks;
import wiresegal.psionup.common.block.spell.BlockConjuredPulsar;
import wiresegal.psionup.common.block.tile.TileConjuredPulsar;
import wiresegal.psionup.common.core.helper.flowcolors.FlowColorHelpers;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceTrickConjurePulsar extends PieceTrick {
	public PieceTrickConjurePulsar(Spell spell) {
		super(spell);
	}
	
	private SpellParam positionParam;
	private SpellParam timeParam;
	
	@Override
	public void initParams() {
		positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
		timeParam = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, true, false);
		SpellHelpers.Building.addAllParams(this, positionParam, timeParam);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		addStats(meta);
	}
	
	protected void addStats(SpellMetadata meta) {
		meta.addStat(EnumSpellStat.POTENCY, 20);
		meta.addStat(EnumSpellStat.COST, 30);
		meta.addStat(EnumSpellStat.COMPLEXITY, 2);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.caster.world.isRemote) return null; //TODO copy to other spells?
		
		BlockPos pos = SpellHelpers.Runtime.getBlockPosFromVectorParam(this, context, positionParam);
		SpellHelpers.Runtime.checkPos(context, pos);
		
		double time = SpellHelpers.Runtime.getNumber(this, context, timeParam, 0);
		
		World world = context.caster.world;
		IBlockState existingState = world.getBlockState(pos);
		IBlockState stateToSet = getStateToSet();
		
		if(existingState != stateToSet) {
			boolean couldPlace = SpellHelpers.Runtime.placeBlock(world, pos, stateToSet, false);
			if(couldPlace) postSet(context, world, pos, time);
		}
		
		return null;
	}
	
	public IBlockState getStateToSet() {
		return ModBlocks.conjuredPulsar.getDefaultState().withProperty(BlockConjuredPulsar.SOLID, true);
	}
	
	public void postSet(SpellContext context, World world, BlockPos pos, double time) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileConjuredPulsar) {
			TileConjuredPulsar pulsar = (TileConjuredPulsar) tile;
			if(time > 0) {
				pulsar.setTime((int) time);
			}
			
			ItemStack playerColorizer = FlowColorHelpers.getColorizer(PsiAPI.getPlayerCAD(context.caster));
			if(!playerColorizer.isEmpty()) pulsar.setColorizer(playerColorizer);
		}
	}
}
