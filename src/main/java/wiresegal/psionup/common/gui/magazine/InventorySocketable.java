package wiresegal.psionup.common.gui.magazine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.spell.SpellCompiler;
import wiresegal.psionup.common.core.helper.IteratorSocketable;

//TODO uhhhHH can I use an item handler
public class InventorySocketable implements IInventory {
	public InventorySocketable(ItemStack stack, int maxBandwidth) {
		this.stack = stack;
		this.maxBandwidth = maxBandwidth;
		
		socketable = (ISocketable) stack.getItem();
	}
	
	public InventorySocketable(ItemStack stack) {
		this(stack, -1);
	}
	
	final ItemStack stack;
	final ISocketable socketable;
	final int maxBandwidth;
	
	private IteratorSocketable getSockerator() {
		return new IteratorSocketable(stack);
	}
	
	private int totalSlots() {
		IteratorSocketable sockerator = getSockerator();
		int count = 0;
		while(sockerator.hasNext()) {
			sockerator.next();
			count++;
		}
		return count - 1;
	}
	
	@Override
	public int getSizeInventory() {
		return totalSlots();
	}
	
	@Override
	public boolean isEmpty() {
		//TODO: Doesnt this feel backwards :/
		IteratorSocketable sockerator = getSockerator();
		while(sockerator.hasNext()) {
			if(!sockerator.next().getRight().isEmpty()) return true;
		}
		return false;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		return socketable.getBulletInSocket(stack, index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack bullet = socketable.getBulletInSocket(stack, index);
		if (!bullet.isEmpty()) socketable.setBulletInSocket(stack, index, ItemStack.EMPTY);
		return bullet;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return decrStackSize(index, 1);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack bullet) {
		socketable.setBulletInSocket(stack, index, bullet);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}
	
	@Override
	public void markDirty() {
		//No-op
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer entityPlayer) {
		//No-op
	}
	
	@Override
	public void closeInventory(EntityPlayer entityPlayer) {
		//No-op
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		//ItemStack item = (stack.getItem()) as? ISpellContainer ?: return false
		ISpellContainer cont;
		if(stack.getItem() instanceof ISpellContainer) {
			cont = (ISpellContainer) stack.getItem();
		} else return false;
		
		if (maxBandwidth == -1) return true;
		
		Spell spell = cont.getSpell(stack);
		SpellCompiler compiler = new SpellCompiler(spell);
		
		return compiler.getCompiledSpell().metadata.stats.getOrDefault(EnumSpellStat.BANDWIDTH, Integer.MAX_VALUE) <= maxBandwidth;
	}
	
	@Override
	public int getField(int i) {
		return 0;
	}
	
	@Override
	public void setField(int i, int i1) {
		//No-op
	}
	
	@Override
	public int getFieldCount() {
		return 0;
	}
	
	@Override
	public void clear() {
		IteratorSocketable sockerator = getSockerator();
		while(sockerator.hasNext()) {
			int index = sockerator.next().getLeft();
			socketable.setBulletInSocket(stack, index, ItemStack.EMPTY);
		}
	}
	
	@Override
	public String getName() {
		return "psionup.container.socketable";
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(getName());
	}
}
