package wiresegal.psionup.common.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class PsionicItem extends Item {
	public PsionicItem(ResourceLocation res) {
		setRegistryName(res);
		setUnlocalizedName(res.getResourceDomain() + "." + res.getResourcePath());
	}
}
