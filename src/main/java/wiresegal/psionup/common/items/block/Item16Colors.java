package wiresegal.psionup.common.items.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import wiresegal.psionup.common.core.PsionicCreativeTab;

public class Item16Colors extends ItemBlock {
	public Item16Colors(Block block) {
		super(block);
		
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab == PsionicCreativeTab.INST) {
			for(int i=0; i < 16; i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
	}
}
