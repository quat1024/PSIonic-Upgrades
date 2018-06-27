package wiresegal.psionup.common.gui;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.common.block.tile.TileCADAssembler;
import vazkii.psi.common.block.tile.container.ContainerCADAssembler;
import vazkii.psi.common.block.tile.container.slot.SlotBullet;
import wiresegal.psionup.common.core.PsionicMethodHandles;
import wiresegal.psionup.common.items.spell.ItemCADMagazine;
import wiresegal.psionup.common.items.spell.ItemFakeCAD;
import wiresegal.psionup.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class AssemblerGUIHijacker {
	@SubscribeEvent
	public static void onOpenContainer(PlayerContainerEvent.Open e) {
		Container c = e.getContainer();
		if(c instanceof ContainerCADAssembler) {
			for(int i=0; i < c.inventorySlots.size(); i++) {
				Slot slot = c.inventorySlots.get(i);
				if(slot instanceof SlotBullet) {
					SlotBullet bulletSlot = (SlotBullet) slot;
					c.inventorySlots.set(i, new SlotBulletReplacement(bulletSlot));
				}
			}
		}
	}
	
	static class SlotBulletReplacement extends SlotBullet {
		public SlotBulletReplacement(SlotBullet delegate) {
			super((TileCADAssembler) delegate.inventory, delegate.getSlotIndex(), delegate.xPos, delegate.yPos, PsionicMethodHandles.getSocketSlot(delegate));
			assembler = (TileCADAssembler) delegate.inventory;
			socketSlot = PsionicMethodHandles.getSocketSlot(delegate);
		}
		
		final TileCADAssembler assembler;
		final int socketSlot;
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			if(!(stack.getItem() instanceof ISpellContainer)) return false;
			
			ISpellContainer spellContainer = (ISpellContainer) stack.getItem();
			
			if(spellContainer.containsSpell(stack) && assembler.isBulletSlotEnabled(socketSlot)) {
				ItemStack socketable = assembler.getStackInSlot(6); //the gun slot
				
				return !spellContainer.isCADOnlyContainer(socketable) || socketable.getItem() instanceof ICAD || socketable.getItem() instanceof ItemFakeCAD || socketable.getItem() instanceof ItemCADMagazine;
			}
			
			return false;
		}
	}
}
