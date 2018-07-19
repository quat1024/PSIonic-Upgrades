package wiresegal.psionup.common.items.component;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import wiresegal.psionup.common.effect.base.PotionPsiChange;
import wiresegal.psionup.common.items.base.ItemComponent;
import wiresegal.psionup.common.lib.LibMisc;

import java.util.List;

@Mod.EventBusSubscriber
public class ItemUnstableBattery extends ItemComponent {
	private static final int PSI_REGEN_BONUS = 10;
	
	@Override
	protected void registerStats() {
		addStat(EnumCADStat.OVERFLOW, 800);	
	}
	
	@Override
	public EnumCADComponent getComponentType(ItemStack itemStack) {
		return EnumCADComponent.BATTERY;
	}
	
	@Override
	protected void addTooltipTags(List<String> tooltip) {
		addTooltipTag(true, tooltip, LibMisc.MOD_ID + ".cadstat.boost_regen", PSI_REGEN_BONUS);
		addTooltipTag(false, tooltip, LibMisc.MOD_ID + ".downsides.on_damage");
	}
	
	@SubscribeEvent
	public static void onDamage(LivingHurtEvent e) {
		EntityLivingBase living = e.getEntityLiving();
		if(living.world.isRemote) return;
		if(!(living instanceof EntityPlayer)) return;
		if(e.getAmount() < 1) return;
		
		EntityPlayer player = (EntityPlayer) living;
		ItemStack cad = PsiAPI.getPlayerCAD(player);
		
		if(!cad.isEmpty()) {
			ICAD icad = (ICAD) cad.getItem();
			
			ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);
			if(!battery.isEmpty() && battery.getItem() instanceof ItemUnstableBattery) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
				data.deductPsi(data.availablePsi + icad.getStoredPsi(cad), 50, true, true);
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
		if(!e.side.isServer()) return;
		
		EntityPlayer player = e.player;
		ItemStack cad = PsiAPI.getPlayerCAD(player);
		
		if(!cad.isEmpty()) {
			ICAD icad = (ICAD) cad.getItem();
			ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);
			
			if(!battery.isEmpty() && battery.getItem() instanceof ItemUnstableBattery) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
				if(data.regenCooldown == 0 && data.availablePsi != data.getTotalPsi()) {
					PotionPsiChange.changePsi(player, PSI_REGEN_BONUS, true);
				}
			}
		}
	}
}
