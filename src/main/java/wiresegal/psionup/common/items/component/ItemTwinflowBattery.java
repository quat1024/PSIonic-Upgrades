package wiresegal.psionup.common.items.component;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import wiresegal.psionup.common.items.base.ItemComponent;
import wiresegal.psionup.common.lib.LibMisc;

import java.util.List;

@Mod.EventBusSubscriber
public class ItemTwinflowBattery extends ItemComponent {
	private static final int PSI_REGEN_BONUS = 5;
	
	@Override
	protected void registerStats() {
		addStat(EnumCADStat.OVERFLOW, 500);
	}
	
	@Override
	public EnumCADComponent getComponentType(ItemStack itemStack) {
		return EnumCADComponent.BATTERY;
	}
	
	@Override
	protected void addTooltipTags(List<String> tooltip) {
		addTooltipTag(true, tooltip, LibMisc.MOD_ID + ".upsides.boost_regen", PSI_REGEN_BONUS);
		addTooltipTag(true, tooltip, LibMisc.MOD_ID + ".upsides.fills_last");
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
		if(!e.side.isServer()) return;
		
		EntityPlayer player = e.player;
		ItemStack cad = PsiAPI.getPlayerCAD(player);
		
		if(!cad.isEmpty()) {
			ICAD icad = (ICAD) cad.getItem();
			ItemStack battery = icad.getComponentInSlot(cad, EnumCADComponent.BATTERY);
			
			if(!battery.isEmpty() && battery.getItem() instanceof ItemTwinflowBattery) {
				PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
				
				if(data.regenCooldown == 0) {
					//TODO
					//PotionPsiChange.addPsiToPlayer(player, PSI_REGEN_BONUS + if (data.availablePsi != data.totalPsi) Math.ceil(data.regenPerTick / 2.0).toInt() else 0, false)
				}
				
				int psiToDump = Math.min(data.getTotalPsi() - data.availablePsi, icad.getStoredPsi(cad));
				if(psiToDump > 0) {
					data.deductPsi(-psiToDump, 0, true);
					icad.consumePsi(cad, psiToDump);
				}
			}
		}
	}
}
