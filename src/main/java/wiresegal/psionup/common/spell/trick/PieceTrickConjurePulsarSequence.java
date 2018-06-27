package wiresegal.psionup.common.spell.trick;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import wiresegal.psionup.common.block.ModBlocks;
import wiresegal.psionup.common.block.spell.BlockConjuredPulsar;
import wiresegal.psionup.common.block.tile.TileConjuredPulsar;
import wiresegal.psionup.common.core.helper.flowcolors.FlowColorHelpers;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceTrickConjurePulsarSequence extends PieceTrick {
	public PieceTrickConjurePulsarSequence(Spell spell) {
		super(spell);
	}
	
	private SpellParam positionParam;
	private SpellParam targetParam;
	private SpellParam maxBlocksParam;
	private SpellParam timeParam;
	
	@Override
	public void initParams() {
		positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
		targetParam = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false);
		maxBlocksParam = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true);
		timeParam = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false);
		SpellHelpers.Building.addAllParams(this, positionParam, targetParam, maxBlocksParam, timeParam);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		
		double maxBlocksValue = SpellHelpers.Compilation.ensurePositiveAndNonzero(this, maxBlocksParam);
		
		meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksValue * 20));
		meta.addStat(EnumSpellStat.COST, (int) (maxBlocksValue * 30));
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.caster.world.isRemote) return null;
		
		Vector3 positionVec = getParamValue(context, positionParam);
		Vector3 targetVec = getParamValue(context, targetParam);
		int maxBlocks = (int) SpellHelpers.Runtime.getNumber(this, context, maxBlocksParam, 0);
		int time = (int) SpellHelpers.Runtime.getNumber(this, context, timeParam, 0);
		
		if(positionVec == null || targetVec == null) {
			throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
		}
		
		int length = (int) Math.min(targetVec.mag(), maxBlocks);
		Vector3 normalizedDirection = targetVec.copy().normalize();
		ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
		
		for(int i=0; i < length; i++) {
			Vector3 blockVector = positionVec.copy().add(normalizedDirection.copy().multiply(i));
			if(!context.isInRadius(blockVector)) {
				throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
			}
			
			World world = context.caster.world;
			BlockPos pos = blockVector.toBlockPos();
			
			if(SpellHelpers.Runtime.placeBlock(world, pos, getStateToSet(), false)) {
				TileEntity tile = world.getTileEntity(pos);
				if(tile instanceof TileConjuredPulsar) {
					TileConjuredPulsar pulsar = (TileConjuredPulsar) tile;
					if(time > 0) {
						pulsar.setTime(time);
					}
					
					ItemStack playerColorizer = FlowColorHelpers.getColorizer(cad);
					if(!playerColorizer.isEmpty()) pulsar.setColorizer(playerColorizer);
				}
			}
		}
		
		return null;
	}
	
	protected IBlockState getStateToSet() {
		return ModBlocks.conjuredPulsar.getDefaultState().withProperty(BlockConjuredPulsar.SOLID, true);
	}
}
