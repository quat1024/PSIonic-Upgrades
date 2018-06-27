package wiresegal.psionup.common.items.component;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.api.exosuit.IExosuitSensor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.client.core.handler.ClientTickHandler;
import wiresegal.psionup.common.lib.LibMisc;
import wiresegal.psionup.common.lib.QuatMiscHelpers;

import java.util.*;

@Mod.EventBusSubscriber
public class ItemBioticSensor extends Item implements IExosuitSensor {
	public ItemBioticSensor() {
		setMaxStackSize(1);
	}
	
	public static final String EVENT_BIOTIC = LibMisc.MOD_ID + ".event.nearby_entities";
	private static final double RANGE = 10d;
	private static final double RANGE_SQ = RANGE * RANGE;
	private static final AxisAlignedBB RANGE_AABB = new AxisAlignedBB(-RANGE, -RANGE, -RANGE, RANGE, RANGE, RANGE);
	
	@Override
	public String getEventType(ItemStack itemStack) {
		return EVENT_BIOTIC;
	}
	
	//TODO Item colors
	
	@Override
	public int getColor(ItemStack itemStack) {
		byte add = (byte) Math.max(Math.sin(ClientTickHandler.ticksInGame * 0.1d) * 96, 0);
		return (add << 16) | (add << 8) | add; 
	}
	
	//TODO this feels messy
	private static final HashMap<EntityPlayer, List<EntityLivingBase>> triggeredBioticsRemote = new HashMap<>();
	private static final HashMap<EntityPlayer, List<EntityLivingBase>> triggeredBioticsNonRemote = new HashMap<>();
	
	private static HashMap<EntityPlayer, List<EntityLivingBase>> getBiotics(World world) {
		return world.isRemote ? triggeredBioticsRemote : triggeredBioticsNonRemote; 
	}
	
	@SubscribeEvent
	public static void entityTick(LivingEvent.LivingUpdateEvent e) {
		if(!(e.getEntityLiving() instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer) e.getEntityLiving();
		World world = player.world;
		
		HashMap<EntityPlayer, List<EntityLivingBase>> triggeredBiotics = getBiotics(world);
		List<EntityLivingBase> triggered = triggeredBiotics.computeIfAbsent(player, k -> new ArrayList<>());
		
		//TODO this is changed, debug this item.
		List<EntityLivingBase> nearbyUntriggeredEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, RANGE_AABB, ent -> {
			if(ent == player) return false;
			if(triggered.contains(ent)) return false;
			return QuatMiscHelpers.distanceSquared(player, ent) <= RANGE_SQ;
		});
		
		for(EntityLivingBase ent : nearbyUntriggeredEntities) {
			PsiArmorEvent.post(new PsiArmorEvent(player, EVENT_BIOTIC, 0d, ent));
		}
		
		triggered.clear();
		triggered.addAll(nearbyUntriggeredEntities);
	}
}
