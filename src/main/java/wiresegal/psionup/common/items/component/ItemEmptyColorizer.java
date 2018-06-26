package wiresegal.psionup.common.items.component;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.ICADColorizer;
import wiresegal.psionup.common.items.base.ItemComponent;

public class ItemEmptyColorizer extends ItemComponent implements ICADColorizer {
	@SideOnly(Side.CLIENT)
	@Override
	public int getColor(ItemStack itemStack) {
		return 0x080808;
	}
}
