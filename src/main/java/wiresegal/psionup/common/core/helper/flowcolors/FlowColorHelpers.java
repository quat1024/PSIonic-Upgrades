package wiresegal.psionup.common.core.helper.flowcolors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import wiresegal.psionup.common.core.helper.LibLibReplacementItemNBTHelper;
import wiresegal.psionup.common.items.ModItems;
import wiresegal.psionup.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class FlowColorHelpers {
	
	@SubscribeEvent
	public static void playerUpdate(LivingEvent.LivingUpdateEvent e) {
		EntityLivingBase ent = e.getEntityLiving();
		if(ent.world.isRemote) return;
		
		if(ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ent;
			ItemStack cad = PsiAPI.getPlayerCAD(player);
			if(cad.isEmpty()) {
				clearColorizer(player);
			} else {
				ItemStack colorizer = ((ICAD)cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
				//TODO: Is this a failsafe, or a "default"?
				if(colorizer.isEmpty()) colorizer = new ItemStack(ModItems.liquidColorizer);
				applyColorizer(player, colorizer);
			}
		}
	}
	
	private static final String TAG_FLOW_COLOR = "FlowColor";
	
	//TODO rename these to "colorizer"
	public static void applyColorizer(EntityPlayer player, ItemStack color) {
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack slot = player.inventory.getStackInSlot(i);
			if(slot.isEmpty()) continue;
			if(!(slot.getItem() instanceof IFlowColorAcceptor)) continue;
			applyColorizer(slot, color);
		}
	}
	
	public static void applyColorizer(ItemStack colorable, ItemStack colorizer) {
		LibLibReplacementItemNBTHelper.setCompound(colorable, TAG_FLOW_COLOR, colorizer.writeToNBT(new NBTTagCompound()));
	}
	
	public static void clearColorizer(EntityPlayer player) {
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack slot = player.inventory.getStackInSlot(i);
			if(slot.isEmpty()) continue;
			if(!(slot.getItem() instanceof IFlowColorAcceptor)) continue;
			clearColorizer(slot);
		}
	}
	
	public static void clearColorizer(ItemStack colorable) {
		LibLibReplacementItemNBTHelper.removeTag(colorable, TAG_FLOW_COLOR);
	}
	
	public static ItemStack getColorizer(ItemStack colorable) {
		NBTTagCompound colorizer = LibLibReplacementItemNBTHelper.getCompound(colorable, TAG_FLOW_COLOR);
		if(colorizer == null) return ItemStack.EMPTY;
		else return new ItemStack(colorizer);
	}
}
