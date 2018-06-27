package wiresegal.psionup.common.spell.operator.block;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceOperator;
import wiresegal.psionup.api.BlockProperties;
import wiresegal.psionup.api.ParamBlockProperties;

public abstract class BasePieceOperatorBlockProperties<T> extends PieceOperator {
	public BasePieceOperatorBlockProperties(Spell spell) {
		super(spell);
	}
	
	private SpellParam properties;
	
	@Override
	public void initParams() {
		properties = new ParamBlockProperties(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false);
		addParam(properties);
	}
	
	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		return getData(context, getParamValue(context, properties));
	}
	
	abstract T getData(SpellContext context, BlockProperties props) throws SpellRuntimeException;
	
	@Override
	public abstract Class<T> getEvaluationType();
}
