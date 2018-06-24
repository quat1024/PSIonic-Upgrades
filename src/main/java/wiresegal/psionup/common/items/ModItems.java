package wiresegal.psionup.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import wiresegal.psionup.common.items.component.*;
import wiresegal.psionup.common.items.spell.*;
import wiresegal.psionup.common.lib.LibMisc;
import wiresegal.psionup.common.lib.LibNames;

public class ModItems {
	
	//CADs and CAD Accessories
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.LIQUID_INK_COLORIZER)
	public static final Item liquidColorizer = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.DRAINED_COLORIZER)
	public static final Item drainedColorizer = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.INLINE_CASTER)
	public static final Item inlineCaster = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.SPELL_MAGAZINE)
	public static final Item spellMagazine = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.WIDE_BAND_SOCKET)
	public static final Item wideBandSocket = Items.AIR;
	
	//Ebony and Ivory Tools and Armor
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.EBONY_HELMET)
	public static final Item ebonyHelmet = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.EBONY_CHEST)
	public static final Item ebonyChest = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.EBONY_LEGS)
	public static final Item ebonyLegs = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.EBONY_BOOTS)
	public static final Item ebonyBoots = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.IVORY_HELMET)
	public static final Item ivoryHelmet = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.IVORY_CHEST)
	public static final Item ivoryChest = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.IVORY_LEGS)
	public static final Item ivoryLegs = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.IVORY_BOOTS)
	public static final Item ivoryBoots = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.EBONY_PICKAXE)
	public static final Item ebonyPickaxe = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.EBONY_SHOVEL)
	public static final Item ebonyShovel = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.EBONY_AXE)
	public static final Item ebonyAxe = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.EBONY_SWORD)
	public static final Item ebonySword = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.IVORY_PICKAXE)
	public static final Item ivoryPickaxe = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.IVORY_SHOVEL)
	public static final Item ivoryShovel = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.IVORY_AXE)
	public static final Item ivoryAxe = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.IVORY_SWORD)
	public static final Item ivorySword = Items.AIR;
	
	//Other Stuff
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.FLASH_RING)
	public static final Item flashRing = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.BIOTIC_SENSOR)
	public static final Item bioticSensor = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.GAUSS_RIFLE)
	public static final Item gaussRifle = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.GAUSS_BULLET)
	public static final Item gaussBullet = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.UNSTABLE_BATTERY)
	public static final Item unstableBattery = Items.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Items.TWINFLOW_BATTERY)
	public static final Item twinflowBattery = Items.AIR;
	
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