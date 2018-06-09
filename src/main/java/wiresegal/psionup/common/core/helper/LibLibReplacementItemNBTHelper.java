package wiresegal.psionup.common.core.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

//TODO when complete with port: remove this dumb name lol
public class LibLibReplacementItemNBTHelper {
	@Nullable
	public static NBTTagCompound getCompound(ItemStack stack, String key) {
		if(!stack.hasTagCompound()) return null;
		return stack.getTagCompound().getCompoundTag(key);
	}
	
	public static void setCompound(ItemStack stack, String key, NBTTagCompound compound) {
		ensureHasCompound(stack);
		stack.getTagCompound().setTag(key, compound);
	}
	
	public static void removeTag(ItemStack stack, String key) {
		if(!stack.hasTagCompound()) return; //WOW guess it's already gone, amazing
		
		stack.getTagCompound().removeTag(key);
	}
	
	private static void ensureHasCompound(ItemStack stack) {
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
	}
}
