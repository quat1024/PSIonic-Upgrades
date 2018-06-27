package wiresegal.psionup.common.spell.trick;


import net.minecraft.util.text.TextComponentString;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import wiresegal.psionup.common.lib.SpellHelpers;

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
		//TODO
		context.caster.sendStatusMessage(new TextComponentString("Debug spamless trick doesnt work yet!"), false);
		return null;
	}
}
