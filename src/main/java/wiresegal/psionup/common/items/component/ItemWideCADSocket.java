package wiresegal.psionup.common.items.component;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import wiresegal.psionup.common.items.base.ItemComponent;

public class ItemWideCADSocket extends ItemComponent {
	@Override
	public EnumCADComponent getComponentType(ItemStack itemStack) {
		return EnumCADComponent.SOCKET;
	}
	
	@Override
	protected void registerStats() {
		addStat(EnumCADStat.BANDWIDTH, 9);
		addStat(EnumCADStat.SOCKETS, 1);
	}
}
