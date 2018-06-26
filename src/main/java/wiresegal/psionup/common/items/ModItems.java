package wiresegal.psionup.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import wiresegal.psionup.common.core.PsionicCreativeTab;
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
		//CADs and CAD Accessories
		
		reg.register(createItem(new ItemFakeCAD(), LibNames.Items.INLINE_CASTER));
		
		//Ebony and Ivory tools and armor
		reg.register(createItem(new ItemFlowTool.Pickaxe(true), LibNames.Items.EBONY_PICKAXE));
		reg.register(createItem(new ItemFlowTool.Shovel(true), LibNames.Items.EBONY_SHOVEL));
		reg.register(createItem(new ItemFlowTool.Axe(true), LibNames.Items.EBONY_AXE));
		reg.register(createItem(new ItemFlowSword(true), LibNames.Items.EBONY_SWORD));
		
		reg.register(createItem(new ItemFlowExosuit.Helmet(true), LibNames.Items.EBONY_HELMET));
		reg.register(createItem(new ItemFlowExosuit.Chestplate(true), LibNames.Items.EBONY_CHEST));
		reg.register(createItem(new ItemFlowExosuit.Leggings(true), LibNames.Items.EBONY_LEGS));
		reg.register(createItem(new ItemFlowExosuit.Boots(true), LibNames.Items.EBONY_BOOTS));
		
		reg.register(createItem(new ItemFlowTool.Pickaxe(false), LibNames.Items.IVORY_PICKAXE));
		reg.register(createItem(new ItemFlowTool.Shovel(false), LibNames.Items.IVORY_SHOVEL));
		reg.register(createItem(new ItemFlowTool.Axe(false), LibNames.Items.IVORY_AXE));
		reg.register(createItem(new ItemFlowSword(false), LibNames.Items.IVORY_SWORD));
		
		reg.register(createItem(new ItemFlowExosuit.Helmet(false), LibNames.Items.IVORY_HELMET));
		reg.register(createItem(new ItemFlowExosuit.Chestplate(false), LibNames.Items.IVORY_CHEST));
		reg.register(createItem(new ItemFlowExosuit.Leggings(false), LibNames.Items.IVORY_LEGS));
		reg.register(createItem(new ItemFlowExosuit.Boots(false), LibNames.Items.IVORY_BOOTS));
		
		//Other Stuff
		
		reg.register(createItem(new ItemFlashRing(), LibNames.Items.FLASH_RING));
		reg.register(createItem(new ItemGaussRifle(), LibNames.Items.GAUSS_RIFLE));
		reg.register(createItem(new Item(), LibNames.Items.GAUSS_BULLET));
		
		reg.register(createItem(new ItemUnstableBattery(), LibNames.Items.UNSTABLE_BATTERY));
		reg.register(createItem(new ItemTwinflowBattery(), LibNames.Items.UNSTABLE_BATTERY));
		
		/*
		reg.register(new ItemLiquidColorizer(new ResourceLocation(LibMisc.MOD_ID, LibNames.Items.LIQUID_INK_COLORIZER))); 
		reg.register(new ItemEmptyColorizer(new ResourceLocation(LibMisc.MOD_ID, LibNames.Items.DRAINED_COLORIZER))); 
		reg.register(new ItemCADMagazine(new ResourceLocation(LibMisc.MOD_ID, LibNames.Items.SPELL_MAGAZINE))); 
		reg.register(new ItemWideCADSocket(new ResourceLocation(LibMisc.MOD_ID, LibNames.Items.WIDE_BAND_SOCKET))); 
		
		
		reg.register(new ItemBioticSensor(new ResourceLocation(LibMisc.MOD_ID, LibNames.Items.BIOTIC_SENSOR))); 
		*/
		
		if(Loader.isModLoaded("botania")) {
			//CompatItems.init(); TODO
		}
	}
	
	private static Item createItem(Item item, String name) {
		return createItem(item, name, true);
	}
	
	private static Item createItem(Item item, String name, boolean showInCreative) {
		ResourceLocation res = new ResourceLocation(LibMisc.MOD_ID, name);
		
		item.setRegistryName(res);
		item.setUnlocalizedName(res.getResourceDomain() + "." + res.getResourcePath());
		
		if(showInCreative) item.setCreativeTab(PsionicCreativeTab.INST);
		
		return item;
	}
}
