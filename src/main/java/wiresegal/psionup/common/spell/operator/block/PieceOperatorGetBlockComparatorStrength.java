package wiresegal.psionup.common.spell.operator.block;

import net.minecraft.util.EnumFacing;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import wiresegal.psionup.api.BlockProperties;
import wiresegal.psionup.common.lib.LibNames;

public class PieceOperatorGetBlockComparatorStrength extends BasePieceOperatorBlockProperties<Double> {
	public PieceOperatorGetBlockComparatorStrength(Spell spell) {
		super(spell);
	}
	
	private SpellParam axisParam;
	
	@Override
	public void initParams() {
		super.initParams();
		
		axisParam = new ParamVector(LibNames.SpellParams.VAZKII_RAY, SpellParam.BLUE, true, false);
		addParam(axisParam);
	}
	
	@Override
	Double getData(SpellContext context, BlockProperties props) throws SpellRuntimeException {
		Vector3 axis = getParamValue(context, axisParam);
		EnumFacing whichWay;
		
		if(axis == null || axis.isZero()) {
			whichWay = EnumFacing.UP;
		} else if(!axis.isAxial()) {
			throw new SpellRuntimeException(LibNames.SpellErrors.NON_AXIAL);
		} else {
			whichWay = EnumFacing.getFacingFromVector((float) axis.x, (float) axis.y, (float) axis.z);
		}
		
		return (double) props.comparatorOutput(whichWay);
	}
	
	@Override
	public Class<Double> getEvaluationType() {
		return Double.class;
	}
}
