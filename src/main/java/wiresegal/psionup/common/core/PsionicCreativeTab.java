package wiresegal.psionup.common.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wiresegal.psionup.common.items.ModItems;
import wiresegal.psionup.common.lib.LibMisc;

public class PsionicCreativeTab extends CreativeTabs {
	public static final PsionicCreativeTab INST = new PsionicCreativeTab();
	
	private PsionicCreativeTab() {
		super(LibMisc.MOD_ID);
		setNoTitle();
		setBackgroundImageName(LibMisc.MOD_ID + ".png");
	}	
	
	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.liquidColorizer);
	}
	
	@Override
	public boolean hasSearchBar() {
		return true;
	}
	
	//TODO displaying items properly
}
