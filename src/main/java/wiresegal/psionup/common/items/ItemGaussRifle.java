package wiresegal.psionup.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import wiresegal.psionup.common.core.helper.flowcolors.IFlowColorAcceptor;
import wiresegal.psionup.common.entity.EntityGaussPulse;

import static wiresegal.psionup.common.entity.EntityGaussPulse.AmmoStatus.*;

public class ItemGaussRifle extends PsionicItem implements IFlowColorAcceptor {
	public ItemGaussRifle(ResourceLocation res) {
		super(res);
		
		setMaxStackSize(1);
	}
	
	private static final int PSI_COST_NO_AMMO = 625;
	private static final int PSI_COST_WITH_AMMO = 200;
	
	//TODO this liblib glowing item shit, reimplement that somehow
	//TODO iitemcolor
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		ItemStack cad = PsiAPI.getPlayerCAD(player);
		if(cad.isEmpty()) return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		
		ItemStack ammo = findAmmo(player);
		boolean creative = player.capabilities.isCreativeMode;
		
		if(creative || data.availablePsi > 0) {
			boolean usingAmmo = !ammo.isEmpty();
			boolean bottomedOutPsi = false;
			
			//Drain psi or consume ammo item
			if(!creative) {
				if(usingAmmo) {
					data.deductPsi(PSI_COST_WITH_AMMO, 10, true);
					ammo.shrink(1);
				} else {
					int cadBattery = ((ICAD)cad.getItem()).getStoredPsi(cad);
					if(data.availablePsi + cadBattery < PSI_COST_NO_AMMO) bottomedOutPsi = true;
					data.deductPsi(PSI_COST_NO_AMMO, (int) (3 * player.getCooldownPeriod()) + (bottomedOutPsi ? 60 : 10), true);
				}
			}
			
			player.swingArm(hand);
			
			EntityGaussPulse.AmmoStatus status;
			
			if(usingAmmo) status = creative ? DEPLETED : AMMO;
			else status = bottomedOutPsi ? BLOOD : NOTAMMO;
			
			EntityGaussPulse ent = new EntityGaussPulse(world, player, status);
			
			player.motionX -= .50 * player.getLookVec().x;
			player.motionY -= .25 * player.getLookVec().y;
			player.motionZ -= .50 * player.getLookVec().z;
			
			if(!world.isRemote) {
				world.spawnEntity(ent);
				
				((EntityPlayerMP) player).getServerWorld().getEntityTracker().sendToTrackingAndSelf(player, new SPacketEntityVelocity(player));
			}
			
			world.playSound(null, player.posX, player.posY, player.posZ, status == BLOOD ? PsiSoundHandler.compileError : PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 1f, 1f);
			
			if(!creative) {
				player.getCooldownTracker().setCooldown(this, (int) player.getCooldownPeriod() * 3);
			}
		}
		
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	private ItemStack findAmmo(EntityPlayer player) {
		//Prioritize held ammo over the rest of the inventory (like a bow)
		
		for(EnumHand hand : EnumHand.values()) {
			ItemStack held = player.getHeldItem(hand);
			if(isAmmo(held)) return held;
		}
		
		for(int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
			ItemStack stackInSlot = player.inventory.getStackInSlot(slot);
			if(isAmmo(stackInSlot)) return stackInSlot;
		}
		
		return ItemStack.EMPTY;
	}
	
	private boolean isAmmo(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == ModItems.gaussBullet;
	}
}
