package wiresegal.psionup.common.spell.trick;


import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import wiresegal.psionup.common.lib.SpellHelpers;
import wiresegal.psionup.common.network.MessageSpamlessChat;
import wiresegal.psionup.common.network.PsionicPacketHandler;

public class PieceTrickDebugSpamless extends PieceTrick {
	public PieceTrickDebugSpamless(Spell spell) {
		super(spell);
	}
	
	private SpellParam targetParam;
	private SpellParam numberParam;
	
	@Override
	public void initParams() {
		targetParam = new ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false);
		numberParam = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, true, false);
		SpellHelpers.Building.addAllParams(this, targetParam, numberParam);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		//No-op (free trick)
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		if(context.caster.world.isRemote || !(context.caster instanceof EntityPlayerMP)) return null;
		
		//Copypasteroo from PieceTrickDebug
		Double numberVal = this.<Double>getParamValue(context, numberParam);
		Object targetVal = getParamValue(context, targetParam);
		
		String s = "null";
		if(targetVal != null)
			s = targetVal.toString();
		
		if(numberVal != null) {
			String numStr = "" + numberVal;
			if(numberVal - numberVal.intValue() == 0) {
				int numInt = numberVal.intValue();
				numStr = "" + numInt;
			}
			
			s = TextFormatting.AQUA + "[" + numStr + "] " + TextFormatting.RESET + s;
		}
		
		//End pasta
		
		TextComponentString component = new TextComponentString(s);
		MessageSpamlessChat message = new MessageSpamlessChat(component);
		PsionicPacketHandler.sendTo(message, (EntityPlayerMP) context.caster);
		
		//context.caster.sendStatusMessage(new TextComponentString("Debug spamless trick doesnt work yet!"), false);
		return null;
	}
}
