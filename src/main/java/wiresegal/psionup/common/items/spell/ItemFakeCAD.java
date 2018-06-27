package wiresegal.psionup.common.items.spell;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import wiresegal.psionup.common.lib.QuatMiscHelpers;
import wiresegal.psionup.common.core.helper.flowcolors.FlowColorHelpers;

public class ItemFakeCAD extends Item implements IPsiAddonTool {
	public ItemFakeCAD() {
		setMaxStackSize(1);
	}
	
	//TODO Liblib glow
	//TODO Item colors
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack held = player.getHeldItem(hand);
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		ItemStack cad = PsiAPI.getPlayerCAD(player);
		
		if(!cad.isEmpty()) {
			ItemStack bullet = getBulletInSocket(held, getSelectedSlot(held));
			if(bullet.isEmpty()) {
				//Craft redstone into psidust
				if(ItemCAD.craft(player, new ItemStack(Items.REDSTONE), new ItemStack(ModItems.material, 1, 0))) {
					if(!world.isRemote) {
						QuatMiscHelpers.emitSoundFromEntity(world, player, PsiSoundHandler.cadShoot, .5f, (float) (.5f + Math.random() * .5));
					}
					
					data.deductPsi(100, 60, true);
					if(data.level == 0) data.levelUp();
					
					return new ActionResult<>(EnumActionResult.SUCCESS, held);
				}
			} else {
				//Cast a spell with the bullet.
				ItemCAD.cast(world, player, data, bullet, cad, 40, 25, 0.5f, null);
				return new ActionResult<>(EnumActionResult.SUCCESS, held);
			}
		}
		
		return super.onItemRightClick(world, player, hand);
	}
	
	//The rest is taken care of in IPsiAddonTool
	@Override
	public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
		return slot < 1;
	}
	
	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem ent) {
		FlowColorHelpers.clearColorizer(ent.getItem());
		return super.onEntityItemUpdate(ent);
	}
}
