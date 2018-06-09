package wiresegal.psionup.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.registries.IForgeRegistry;
import wiresegal.psionup.common.items.component.*;
import wiresegal.psionup.common.items.spell.*;
import wiresegal.psionup.common.lib.LibNames;

public class ModItems {
	public static void register(IForgeRegistry<Item> reg) {
		reg.register(new ItemLiquidColorizer(LibNames.Items.LIQUID_INK_COLORIZER));
		reg.register(new ItemEmptyColorizer(LibNames.Items.DRAINED_COLORIZER));
		reg.register(new ItemFakeCAD(LibNames.Items.INLINE_CASTER));
		reg.register(new ItemCADMagazine(LibNames.Items.SPELL_MAGAZINE));
		reg.register(new ItemWideCADSocket(LibNames.Items.WIDE_BAND_SOCKET));
		
		reg.register(new ItemFlowExosuit.Helmet(LibNames.Items.EBONY_HELMET, true));
		reg.register(new ItemFlowExosuit.Chest(LibNames.Items.EBONY_CHEST, true));
		reg.register(new ItemFlowExosuit.Legs(LibNames.Items.EBONY_LEGS, true));
		reg.register(new ItemFlowExosuit.Boots(LibNames.Items.EBONY_BOOTS, true));
		
		reg.register(new ItemFlowExosuit.Helmet(LibNames.Items.IVORY_HELMET, false));
		reg.register(new ItemFlowExosuit.Chest(LibNames.Items.IVORY_CHEST, false));
		reg.register(new ItemFlowExosuit.Legs(LibNames.Items.IVORY_LEGS, false));
		reg.register(new ItemFlowExosuit.Boots(LibNames.Items.IVORY_BOOTS, false));
		
		reg.register(new ItemFlashRing(LibNames.Items.FLASH_RING));
		
		reg.register(new ItemFlowTool.Pickaxe(LibNames.Items.EBONY_PICKAXE, true));
		reg.register(new ItemFlowTool.Shovel(LibNames.Items.EBONY_SHOVEL, true));
		reg.register(new ItemFlowTool.Axe(LibNames.Items.EBONY_AXE, true));
		reg.register(new ItemFlowSword(LibNames.Items.EBONY_SWORD, true));
		
		reg.register(new ItemFlowTool.Pickaxe(LibNames.Items.IVORY_PICKAXE, false));
		reg.register(new ItemFlowTool.Shovel(LibNames.Items.IVORY_SHOVEL, false));
		reg.register(new ItemFlowTool.Axe(LibNames.Items.IVORY_AXE, false));
		reg.register(new ItemFlowSword(LibNames.Items.IVORY_SWORD, false));
		
		reg.register(new ItemBioticSensor(LibNames.Items.BIOTIC_SENSOR));
		
		reg.register(new ItemGaussRifle(LibNames.Items.GAUSS_RIFLE));
		
		reg.register(new ItemMod(LibNames.Items.GAUSS_BULLET));
		
		reg.register(new ItemUnstableBattery(LibNames.Items.UNSTABLE_BATTERY));
		reg.register(new ItemTwinflowBattery(LibNames.Items.TWINFLOW_BATTERY));
		
		if(Loader.isModLoaded("botania")) {
			//CompatItems.init(); TODO
		}
	}
}
