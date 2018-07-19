package wiresegal.psionup.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import wiresegal.psionup.common.items.component.botania.ItemBlasterAssembly;
import wiresegal.psionup.common.lib.LibNames;

public class BotaniaCompatItems {
	public static ItemBlasterAssembly blaster;
	
	public static void register(IForgeRegistry<Item> reg) {
		blaster = ModItems.createItem(new ItemBlasterAssembly(), LibNames.Items.LIVINGWOOD_CAD);
		reg.register(blaster);
		
		//Todo something about mod brews
		//Todo some compat recipes
	}
}
