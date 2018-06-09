package wiresegal.psionup.common.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import wiresegal.psionup.common.block.spell.BlockConjuredPulsar;
import wiresegal.psionup.common.block.spell.BlockConjuredStar;
import wiresegal.psionup.common.block.tile.*;
import wiresegal.psionup.common.lib.LibMisc;
import wiresegal.psionup.common.lib.LibNames;

public class ModBlocks {
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Blocks.CONJURED_PULSAR)
	public static final Block conjuredPulsar = Blocks.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Blocks.CONJURED_STAR)
	public static final Block conjuredStar = Blocks.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Blocks.BRIGHT_PLATE)
	public static final Block brightPlate = Blocks.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Blocks.DARK_PLATE)
	public static final Block darkPlate = Blocks.AIR;
	
	@GameRegistry.ObjectHolder(LibMisc.MOD_ID + ":" + LibNames.Blocks.CAD_CASE)
	public static final Block cadCase = Blocks.AIR;
	
	public static void register(IForgeRegistry<Block> reg) {
		reg.register(new BlockConjuredPulsar(LibNames.Blocks.CONJURED_PULSAR));
		reg.register(new BlockConjuredStar(LibNames.Blocks.CONJURED_STAR));
		
		reg.register(new BlockPlate(LibNames.Blocks.BRIGHT_PLATE));
		reg.register(new BlockPlate(LibNames.Blocks.DARK_PLATE));
		
		reg.register(new BlockCADCase(LibNames.Blocks.CAD_CASE));
		
		GameRegistry.registerTileEntity(TileConjuredPulsar.class, LibMisc.MOD_ID + ":pulsar");
		GameRegistry.registerTileEntity(TileCracklingStar.class, LibMisc.MOD_ID + ":star");
		GameRegistry.registerTileEntity(TileCADCase.class, LibMisc.MOD_ID + ":case");
		
		//TODO: ItemBlocks
	}
}
