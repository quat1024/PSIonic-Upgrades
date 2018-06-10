package wiresegal.psionup.common.spell.trick.botania;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import wiresegal.psionup.api.enabling.ITrickEnablerComponent;
import wiresegal.psionup.api.enabling.PieceComponentTrick;
import wiresegal.psionup.api.enabling.botania.IBlasterComponent;
import wiresegal.psionup.common.spell.SpellHelpers;

public class PieceTrickFormBurst extends PieceComponentTrick {
	public PieceTrickFormBurst(Spell spell) {
		super(spell);
	}
	
	private SpellParam positionParam;
	private SpellParam rayParam;
	
	private static int MANA_PER_BURST = 120;
	
	@Override
	public void initParams() {
		positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
		rayParam = new ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false);
		
		SpellHelpers.Building.addAllParams(this, positionParam, rayParam);
	}
	
	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
		super.addToMetadata(meta);
		
		meta.addStat(EnumSpellStat.COST, 400);
		meta.addStat(EnumSpellStat.POTENCY, 150);
	}
	
	@Override
	public ITrickEnablerComponent.EnableResult acceptsPiece(ItemStack component, ItemStack cad, SpellContext context, Spell spell, int x, int y) {
		if(component.getItem() instanceof IBlasterComponent) {
			return ITrickEnablerComponent.EnableResult.fromBoolean(ManaItemHandler.requestManaExact(cad, context.caster, MANA_PER_BURST, true));
		} else return ITrickEnablerComponent.EnableResult.NOT_ENABLED;
	}
	
	@Override
	public Object executeIfAllowed(SpellContext context) throws SpellRuntimeException {
		return null;//TODO
	}
}
