package wiresegal.psionup.common.spell.trick;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.entity.EntitySpellCircle;
import wiresegal.psionup.common.lib.SpellHelpers;

public class PieceTrickBreakLoop extends PieceTrick {
	public PieceTrickBreakLoop(Spell spell) {
		super(spell);
	}
	
	private SpellParam valueParam;
	
	@Override
	public void initParams() {
		valueParam = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, false);
		addParam(valueParam);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta); //different from original, TODO do simple tricks like this in Psi also skip super? was it intentional?
		meta.addStat(EnumSpellStat.COMPLEXITY, 1);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		double value = SpellHelpers.Runtime.getNumber(this, context, valueParam, 0);
		
		if(Math.abs(value) < 1.0) {
			if(context.focalPoint != context.caster) {
				if(context.focalPoint instanceof EntitySpellCircle) {
					//Cause the spell circle to finish early
					EntitySpellCircle circle = (EntitySpellCircle) context.focalPoint;
					NBTTagCompound circleNBT = circle.writeToNBT(new NBTTagCompound());
					circleNBT.setInteger("timesCast", 20);
					circleNBT.setInteger("timesAlive", 100);
					circle.readFromNBT(circleNBT);
				} else {
					//I dunno what's casting this spell, just get rid of it
					context.focalPoint.setDead();
				}
			} else {
				//Casting from loopcast or something
				PlayerDataHandler.get(context.caster).stopLoopcast();
			}
			
			context.stopped = true;
		}
		return null;
	}
	
	
}
