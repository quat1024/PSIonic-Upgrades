package wiresegal.psionup.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class PsionicItemBlock extends ItemBlock {
	public PsionicItemBlock(Block block) {
		super(block);
		
		//Thanks MOJANG
		setRegistryName(block.getRegistryName());
	}
}
