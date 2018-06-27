package wiresegal.psionup.common.lib;

public class LibNames {
	public static class Items {
		public static final String LIQUID_INK_COLORIZER = "liquid_colorizer";
		public static final String DRAINED_COLORIZER = "empty_colorizer";
		public static final String INLINE_CASTER = "inline_caster";
		public static final String SPELL_MAGAZINE = "magazine";
		public static final String WIDE_BAND_SOCKET = "wide_socket";
		
		public static final String EBONY_HELMET = "ebony_helmet";
		public static final String EBONY_CHEST = "ebony_chestplate";
		public static final String EBONY_LEGS = "ebony_leggings";
		public static final String EBONY_BOOTS = "ebony_boots";
		
		public static final String IVORY_HELMET = "ivory_helmet";
		public static final String IVORY_CHEST = "ivory_chestplate";
		public static final String IVORY_LEGS = "ivory_leggings";
		public static final String IVORY_BOOTS = "ivory_boots";
		
		public static final String EBONY_PICKAXE = "ebony_pickaxe";
		public static final String EBONY_SHOVEL = "ebony_shovel";
		public static final String EBONY_AXE = "ebony_axe";
		public static final String EBONY_SWORD = "ebony_sword";
		
		public static final String IVORY_PICKAXE = "ivory_pickaxe";
		public static final String IVORY_SHOVEL = "ivory_shovel";
		public static final String IVORY_AXE = "ivory_axe";
		public static final String IVORY_SWORD = "ivory_sword";
		
		public static final String FLASH_RING = "flash_ring";
		
		public static final String BIOTIC_SENSOR = "biotic_sensor";
		
		public static final String GAUSS_RIFLE = "gauss_rifle";
		
		public static final String GAUSS_BULLET = "gauss_bullet";
		
		public static final String LIVINGWOOD_CAD = "cad_assembly_mana_blaster";
		public static final String LIVINGWOOD_CAD_MODEL = "cad_mana_blaster";
		
		public static final String UNSTABLE_BATTERY = "unstable_battery";
		public static final String TWINFLOW_BATTERY = "twinflow_battery";
	}
	
	public static class Blocks {
		public static final String CONJURED_PULSAR = "conjured_pulsar";
		public static final String CONJURED_STAR = "conjured_star";
		
		public static final String BRIGHT_PLATE = "bright_plate";
		public static final String DARK_PLATE = "dark_plate";
		
		public static final String CAD_CASE = "cad_case";
	}
	
	public static class Spell {
		public static final String CONJURE_PULSAR = "conjure_pulsar";
		public static final String CONJURE_PULSAR_SEQUENCE = "conjure_pulsar_sequence";
		public static final String CONJURE_PULSAR_LIGHT = "conjure_pulsar_light";
		
		public static final String PLANAR_NORMAL_VECTOR = "planar_norm";
		public static final String VECTOR_ROTATE = "vector_rotate";
		
		public static final String STRONG_VECTOR_RAYCAST = "strong_vector_raycast";
		public static final String STRONG_VECTOR_RAYCAST_AXIS = "strong_vector_raycast_axis";
		
		public static final String VECTOR_FALLBACK = "vector_fallback";
		
		public static final String PARTICLE_TRAIL = "particle_trail";
		
		public static final String CONJURE_CRACKLE = "conjure_crackle";
		
		public static final String BREAK_LOOP = "loopcast_break";
		
		public static final String LIST_SIZE = "list_size";
		
		public static final String GET_PROPERTIES = "get_properties";
		public static final String GET_HARDNESS = "get_hardness";
		public static final String GET_LIGHT = "get_light";
		public static final String GET_SOLIDITY = "get_solidity";
		public static final String GET_COMPARATOR = "get_comparator";
		public static final String EQUALITY = "operator_equality";
		public static final String BREAK_BOX = "break_box";
		public static final String CLONE_BOX = "clone_box";
		
		public static final String SPAMLESS = "spamless_debug";
		
		public static final String MAKE_BURST = "make_mana_burst";
		public static final String WILD_DRUM = "drum_of_the_wild";
		public static final String CANOPY_DRUM = "drum_of_the_canopy";
		public static final String COVERING_HORN = "horn_of_the_covering";
		public static final String GATHERING_DRUM = "drum_of_the_gathering";
	}
	
	public static class SpellErrors {
		public static final String NON_AXIAL = LibMisc.MOD_ID + ".spellerror.nonaxial";
	}
	
	public static class SpellParams {
		public static final String TARGET_1 = LibMisc.MOD_ID + ".spellparam.target_1";
		public static final String TARGET_2 = LibMisc.MOD_ID + ".spellparam.target_2";
		public static final String FALLBACK = LibMisc.MOD_ID + ".spellparam.fallback";
		public static final String AXIS = LibMisc.MOD_ID + ".spellparam.axis";
		public static final String ANGLE = LibMisc.MOD_ID + ".spellparam.angle";
		
		//One Vazkii forgot, apparently.
		public static final String VAZKII_RAY = "psi.spellparam.ray";
	}
	
	public static class PieceGroups {
		public static final String ALTERNATE_CONJURATION = LibMisc.MOD_ID + ".redstone_conjuration";
		public static final String SECONDARY_VECTOR_OPERATORS = LibMisc.MOD_ID + ".secondary_vectors";
		public static final String BLOCK_PROPERTIES = LibMisc.MOD_ID + ".block_properties";
		
		public static final String MANA_PSIONICS = LibMisc.MOD_ID + ".mana_psionics";
	}
	
	public static class Entities {
		public static final String GAUSS_PULSE = "gauss_pulse";
	}
	
	public static class Potions {
		public static final String PSISHOCK = "psishock";
		public static final String PSISHOCK_STRONG = "psishock_strong";
		public static final String PSISHOCK_LONG = "psishock_long";
		
		public static final String PSIPULSE = "psipulse";
		public static final String PSIPULSE_STRONG = "psipulse_strong";
		public static final String PSIPULSE_LONG = "psipulse_long";
	}
	
	public static String[] Colors = new String[]{
					"white", "orange", "magenta", "light_blue",
					"yellow", "lime", "pink", "gray",
					"silver", "cyan", "purple", "blue",
					"brown", "green", "red", "black"};
}
