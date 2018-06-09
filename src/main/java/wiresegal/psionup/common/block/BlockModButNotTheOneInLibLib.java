package wiresegal.psionup.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import wiresegal.psionup.common.lib.LibMisc;

public class BlockModButNotTheOneInLibLib extends Block {
	public BlockModButNotTheOneInLibLib(String name, Material mat) {
		super(mat);
		
		setRegistryName(LibMisc.MOD_ID + ":" + name);
		setUnlocalizedName(LibMisc.MOD_ID + "." + name);
	}
}
