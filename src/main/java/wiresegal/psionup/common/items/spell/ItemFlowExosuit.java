package wiresegal.psionup.common.items.spell;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.ItemPsimetalTool;
import wiresegal.psionup.client.core.ClientHelpers;
import wiresegal.psionup.common.core.helper.flowcolors.FlowColorHelpers;
import wiresegal.psionup.common.lib.ItemNBTHelpers;
import wiresegal.psionup.common.lib.LibMisc;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemFlowExosuit extends ItemArmor implements IPsiAddonTool, IPsiEventArmor {
	private ItemFlowExosuit(EntityEquipmentSlot slot, boolean ebony) {
		super(PsiAPI.PSIMETAL_ARMOR_MATERIAL, -1, slot);
		this.ebony = ebony;
	}
	
	final boolean ebony;
	
	//TODO liblib glow
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack armorPiece) {
		ItemPsimetalTool.regen(armorPiece, player, false);
	}
	
	public void cast(ItemStack armorPiece, PsiArmorEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		ItemStack cad = PsiAPI.getPlayerCAD(player);
		
		if(!cad.isEmpty()) {
			ItemStack bullet = getBulletInSocket(armorPiece, getSelectedSlot(armorPiece));
			ItemCAD.cast(player.world, player, data, bullet, cad, getCastCooldown(armorPiece), 0, getCastVolume(), spellContext -> {
				spellContext.tool = armorPiece;
				spellContext.attackingEntity = event.attacker;
				spellContext.damageTaken = event.damage;
			});
		}
	}
	
	@Override
	public void onEvent(ItemStack armorPiece, PsiArmorEvent event) {
		if(event.type == getEvent(armorPiece)) {
			cast(armorPiece, event);
		}
	}
	
	protected float getCastVolume() {
		return 0.025f;
	}
	
	abstract int getCastCooldown(ItemStack armorPiece);
	abstract String getEvent(ItemStack armorPiece);
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack armorPiece, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
		if(GuiScreen.isShiftKeyDown()) {
			String socketedName = I18n.translateToLocal(ISocketable.getSocketedItemName(armorPiece, "psimisc.none"));
			tooltip.add(I18n.translateToLocalFormatted("psimisc.spellSelected", socketedName));
			
			tooltip.add(I18n.translateToLocal(getEvent(armorPiece)));
		}
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if(repair.getItem() == ModItems.material) {
			return repair.getItemDamage() == (ebony ? 4 : 5);
		} else return super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem ent) {
		FlowColorHelpers.clearColorizer(ent.getItem());
		return super.onEntityItemUpdate(ent);
	}
	
	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if(type != null && type.equals("overlay")) {
			return LibMisc.MOD_ID + ":textures/model/" + (ebony ? "ebony" : "ivory") + "_exosuit.png";
		} else return "psi:textures/model/psimetal_exosuit_sensor.png";
	}
	
	@SideOnly(Side.CLIENT)
	public int getItemColor(ItemStack stack, int layer) {
		if(layer == 0) {
			return ClientHelpers.getFlowColor(stack);
		} else if (layer == 1) {
			return getColor(stack);
		} else return 0xFFFFFF;
	}
	
	/*
	@Override
	public boolean hasColor(ItemStack stack) {
		return true;
	}
	
	@Override
	public int getColor(ItemStack stack) {
		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}*/
	
	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
	}
	
	public static class Helmet extends ItemFlowExosuit implements ISensorHoldable {
		public Helmet(boolean ebony) {
			super(EntityEquipmentSlot.HEAD, ebony);
		}
		
		@Override
		int getCastCooldown(ItemStack armorPiece) {
			return 40;
		}
		
		@Override
		String getEvent(ItemStack hemlet) {
			ItemStack sensor = getAttachedSensor(hemlet);
			if(!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor) {
				return ((IExosuitSensor)sensor.getItem()).getEventType(hemlet);
			} else return PsiArmorEvent.DAMAGE;
		}
		
		@Override
		public int getColor(ItemStack helmet) {
			ItemStack sensor = getAttachedSensor(helmet);
			if(!sensor.isEmpty() && sensor.getItem() instanceof IExosuitSensor) {
				return ((IExosuitSensor)sensor.getItem()).getColor(sensor);
			} else return super.getColor(helmet);
		}
		
		@Override
		public ItemStack getAttachedSensor(ItemStack helmet) {
			return ItemNBTHelpers.getItemStack(helmet, "Sensor");
		}
		
		@Override
		public void attachSensor(ItemStack helmet, ItemStack sensor) {
			ItemNBTHelpers.setItemStack(helmet, "Sensor", sensor);
		}
		
		@Override
		public boolean hasContainerItem() {
			return true;
		}
		
		@Override
		public ItemStack getContainerItem(ItemStack helmet) {
			return getAttachedSensor(helmet);
		}
	}
	
	public static class Chestplate extends ItemFlowExosuit {
		public Chestplate(boolean ebony) {
			super(EntityEquipmentSlot.CHEST, ebony);
		}
		
		@Override
		int getCastCooldown(ItemStack armorPiece) {
			return 5;
		}
		
		@Override
		String getEvent(ItemStack armorPiece) {
			return PsiArmorEvent.DAMAGE;
		}
	}
	
	public static class Leggings extends ItemFlowExosuit {
		public Leggings(boolean ebony) {
			super(EntityEquipmentSlot.LEGS, ebony);
		}
		
		@Override
		int getCastCooldown(ItemStack armorPiece) {
			return 0;
		}
		
		@Override
		String getEvent(ItemStack armorPiece) {
			return PsiArmorEvent.TICK;
		}
		
		@Override
		protected float getCastVolume() {
			return 0f;
		}
	}
	
	public static class Boots extends ItemFlowExosuit {
		public Boots(boolean ebony) {
			super(EntityEquipmentSlot.FEET, ebony);
		}
		
		@Override
		int getCastCooldown(ItemStack armorPiece) {
			return 5;
		}
		
		@Override
		String getEvent(ItemStack armorPiece) {
			return PsiArmorEvent.JUMP;
		}
	}
}
