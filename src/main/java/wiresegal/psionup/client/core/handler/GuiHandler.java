package wiresegal.psionup.client.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import wiresegal.psionup.client.gui.flashring.GuiFlashRing;
import wiresegal.psionup.common.block.BlockCADCase;
import wiresegal.psionup.common.gui.cadcase.ContainerCADCase;
import wiresegal.psionup.common.gui.magazine.ContainerCADMagazine;
import wiresegal.psionup.common.items.spell.ItemCADMagazine;
import wiresegal.psionup.common.items.spell.ItemFlashRing;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
	private static final int GUI_CASE = 0;
	private static final int GUI_MAGAZINE = 1;
	private static final int GUI_FLASH_RING = 2;
	
	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id) {
			case GUI_CASE: {
				ItemStack stack = getStack(player, BlockCADCase.class);
				if(!stack.isEmpty()) return new ContainerCADCase(player, stack);
				break;
			}
			
			case GUI_MAGAZINE: {
				ItemStack stack = getStack(player, ItemCADMagazine.class);
				if(!stack.isEmpty()) return new ContainerCADMagazine(player, stack);
			}
		}
		
		return null;
	}
	
	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch(id) {
			case GUI_CASE: {
				ItemStack stack = getStack(player, BlockCADCase.class);
				if(!stack.isEmpty()) return new ContainerCADCase(player, stack);
				break;
			}
			
			case GUI_MAGAZINE: {
				ItemStack stack = getStack(player, ItemCADMagazine.class);
				if(!stack.isEmpty()) return new ContainerCADMagazine(player, stack);
			}
			
			case GUI_FLASH_RING: {
				ItemStack stack = getStack(player, ItemFlashRing.class);
				if(!stack.isEmpty()) return new GuiFlashRing(player, stack);
			}
		}
		
		return null;
	}
	
	private static ItemStack getStack(EntityPlayer player, Class itemClass) {
		for(EnumHand hand : EnumHand.values()) {
			ItemStack heldStack = player.getHeldItem(hand);
			if(heldStack.isEmpty()) continue;
			
			Item heldItem = heldStack.getItem();
			
			if(itemClass.isInstance(heldItem)) return heldStack;
			
			if(heldItem instanceof ItemBlock) {
				ItemBlock ib = (ItemBlock) heldItem;
				if(itemClass.isInstance(ib)) return heldStack;
			}
		}
		
		return ItemStack.EMPTY;
	}
}
