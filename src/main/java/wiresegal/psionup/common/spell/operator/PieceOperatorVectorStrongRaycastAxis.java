package wiresegal.psionup.common.spell.operator;

import net.minecraft.util.math.RayTraceResult;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;

public class PieceOperatorVectorStrongRaycastAxis extends PieceOperatorVectorStrongRaycast {
	public PieceOperatorVectorStrongRaycastAxis(Spell spell) {
		super(spell);
	}
	
	@Override
	protected Vector3 createResult(RayTraceResult res) {
		return new Vector3(res.sideHit.getFrontOffsetX(), res.sideHit.getFrontOffsetY(), res.sideHit.getFrontOffsetZ());
	}
}
