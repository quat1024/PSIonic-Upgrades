package wiresegal.psionup.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import wiresegal.psionup.common.lib.LibMisc;

public class BlockModButNotTheOneInLibLib extends Block {
	public BlockModButNotTheOneInLibLib(String name, Material mat) {
		this(name, mat, mat.getMaterialMapColor());
	}
	
	public BlockModButNotTheOneInLibLib(String name, Material mat, MapColor color) {
		super(mat, color);
		
		setRegistryName(LibMisc.MOD_ID + ":" + name);
		setUnlocalizedName(LibMisc.MOD_ID + "." + name);
	}
}
