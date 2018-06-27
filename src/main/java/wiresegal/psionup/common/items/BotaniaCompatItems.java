package wiresegal.psionup.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import wiresegal.psionup.common.items.component.botania.ItemBlasterAssembly;
import wiresegal.psionup.common.lib.LibNames;

public class BotaniaCompatItems {
	//TODO: Look in to objectholder replacements kinda? I don't want FML to discover my objectholder and run in to classloading issues when Botania is not present
	//Now I can see why objectholder is becoming less of a thing in 1.13 lmao
	public static ItemBlasterAssembly blaster;
	
	public static void register(IForgeRegistry<Item> reg) {
		blaster = ModItems.createItem(new ItemBlasterAssembly(), LibNames.Items.LIVINGWOOD_CAD);
		reg.register(blaster);
		
		//Todo something about mod brews
		//Todo some compat recipes
	}
}
