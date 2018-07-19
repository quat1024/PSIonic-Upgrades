package wiresegal.psionup.common.effect.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;

public abstract class PotionPsiChange extends Potion {
	public PotionPsiChange(boolean bad, int color) {
		super(bad, color);
	}
	
	protected abstract int getBaseChangeAmount();
	protected abstract int getAmplifierAmount();
	
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		if(entity instanceof EntityPlayer) {
			int psiChangeAmount = getBaseChangeAmount() + (getAmplifierAmount() * amplifier);
			
			changePsi((EntityPlayer) entity, psiChangeAmount, true);
		}
	}
	
	//TODO: split into a helper class.
	public static void changePsi(EntityPlayer player, int psiChangeAmount, boolean sendSyncPacket) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		
		if(psiChangeAmount < 0) {
			data.deductPsi(-psiChangeAmount, 40, true, true);
		} else {
			ItemStack cad = PsiAPI.getPlayerCAD(player);
			if(cad.isEmpty()) return;
			ICAD icad = (ICAD) cad.getItem();
			
			int overflow = icad.getStatValue(cad, EnumCADStat.OVERFLOW);
			int stored = icad.getStoredPsi(cad);
			
			if(stored < overflow) {
				icad.regenPsi(cad, Math.max(1, psiChangeAmount / 2)); //TODO why /2?
			} else if(data.getAvailablePsi() < data.getTotalPsi()) {
				data.availablePsi = Math.min(data.getTotalPsi(), data.getAvailablePsi() + psiChangeAmount);
				data.save();
				if(sendSyncPacket && player.world.isRemote && player instanceof EntityPlayerMP) {
					NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) player);
				}
			}
		}
	}
}
