package wiresegal.psionup.common.gui.magazine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.ISpellContainer;
import wiresegal.psionup.common.items.spell.ItemCADMagazine;
import wiresegal.psionup.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public class ContainerCADMagazine extends Container {
	private static final List<Pair<Integer, Integer>> innerSlotPositions;
	private static final List<Pair<Integer, Integer>> outerSlotPositions;
	
	static {
		innerSlotPositions = new ArrayList<>();
		
		innerSlotPositions.add(Pair.of(135, 88));
		innerSlotPositions.add(Pair.of(129, 109));
		innerSlotPositions.add(Pair.of(110, 128));
		innerSlotPositions.add(Pair.of(89, 134));
		innerSlotPositions.add(Pair.of(68, 128));
		innerSlotPositions.add(Pair.of(49, 109));
		innerSlotPositions.add(Pair.of(43, 88));
		innerSlotPositions.add(Pair.of(49, 67));
		innerSlotPositions.add(Pair.of(68, 48));
		innerSlotPositions.add(Pair.of(89, 42));
		innerSlotPositions.add(Pair.of(110, 48));
		innerSlotPositions.add(Pair.of(129, 67));
		
		outerSlotPositions = new ArrayList<>();
		
		outerSlotPositions.add(Pair.of(173, 88));
		outerSlotPositions.add(Pair.of(159, 131));
		outerSlotPositions.add(Pair.of(130, 160));
		outerSlotPositions.add(Pair.of(89, 171));
		outerSlotPositions.add(Pair.of(48, 160));
		outerSlotPositions.add(Pair.of(19, 131));
		outerSlotPositions.add(Pair.of(5, 88));
		outerSlotPositions.add(Pair.of(19, 45));
		outerSlotPositions.add(Pair.of(48, 16));
		outerSlotPositions.add(Pair.of(89, 5));
		outerSlotPositions.add(Pair.of(130, 16));
		outerSlotPositions.add(Pair.of(159, 45));
	}
	
	public ContainerCADMagazine(EntityPlayer player, ItemStack stack) {
		this.player = player;
		this.stack = stack;
		
		inventory = new InventorySocketable(stack, ItemCADMagazine.getBandwidth(stack));
		cadInventory = new InventorySocketable(PsiAPI.getPlayerCAD(player), -1);
		
		int index = 0;
		
		for(Pair<Integer, Integer> p : innerSlotPositions) {
			addSlotToContainer(new SlotCustomBullet(cadInventory, index, p.getLeft(), p.getRight(), index, true));
		}
		
		for(Pair<Integer, Integer> p : outerSlotPositions) {
			addSlotToContainer(new SlotCustomBullet(cadInventory, index, p.getLeft(), p.getRight(), index, false));
		}
	}
	
	final EntityPlayer player;
	final ItemStack stack;
	final InventorySocketable inventory;
	final InventorySocketable cadInventory;
	
	@Override
	public boolean canInteractWith(EntityPlayer p) {
		return inventory.isUsableByPlayer(p);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			int magazineStart = 0;
			int magazineEnd = innerSlotPositions.size() - 1;
			int cadStart = magazineEnd + 1;
			int cadEnd = cadStart + outerSlotPositions.size() - 1;
			
			dontNotify = true;
			notifyOnce = true;
			if (index > magazineEnd) {
				if (!mergeItemStack(itemstack1, magazineStart, magazineEnd, false)) {
					return ItemStack.EMPTY; // CAD -> Magazine
				}
			} else if (!mergeItemStack(itemstack1, cadStart, cadEnd, false))
				return ItemStack.EMPTY; // Magazine -> CAD
			dontNotify = false;
			notifyOnce = false;
			
			slot.onSlotChanged();
			
			if (itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
			else if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			
			slot.onTake(playerIn, itemstack1);
		}
		
		return itemstack;
	}
	
	//Hey look, weird side effects.
	public boolean dontNotify = false;
	public boolean notifyOnce = false;
	public float tooltipTime = 0;
	public String tooltipText = "";
	
	public class SlotCustomBullet extends Slot {
		public SlotCustomBullet(InventorySocketable socketable, int index, int xPosition, int yPosition, int socketSlot, boolean dark) {
			super(socketable, index, xPosition, yPosition);
			this.socketable = socketable;
			this.index = index;
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			this.socketSlot = socketSlot;
			this.dark = dark;
		}
		
		public final InventorySocketable socketable;
		public final int index;
		public final int xPosition;
		public final int yPosition;
		public final int socketSlot;
		public final boolean dark;
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			if(stack.getItem() instanceof ISpellContainer) {
				ISpellContainer container = (ISpellContainer) stack.getItem();
				if(container.containsSpell(stack) && isEnabled()) {
					boolean ret = socketable.isItemValidForSlot(socketSlot, stack);
					
					if(!ret && (!dontNotify || notifyOnce) && player.world.isRemote) {
						tooltipTime = 80;
						tooltipText = LibMisc.MOD_ID + ".misc.too_complex";
						if(notifyOnce) notifyOnce = false;
					} else if (ret) {
						tooltipTime = 0;
					}
					
					return ret;
				}
			}
			
			return false;
		}
		
		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			boolean ret = socketable.isItemValidForSlot(socketSlot, stack);
			
			if(!ret && (!dontNotify || notifyOnce) && player.world.isRemote) {
				tooltipTime = 80;
				tooltipText = LibMisc.MOD_ID + ".misc.too_complex";
				if(notifyOnce) notifyOnce = false;
			} else if (ret) {
				tooltipTime = 0;
			}
			
			return ret;
		}
		
		//TODO is this an acceptable replacement for the non-1.12 "canBeHovered"?
		@SideOnly(Side.CLIENT)
		@Override
		public boolean isEnabled() {
			return socketSlot <= socketable.getSizeInventory();
		}
	}
}
