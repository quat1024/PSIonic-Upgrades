package wiresegal.psionup.common.items.spell;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.ItemSpellDrive;
import wiresegal.psionup.client.core.handler.GuiHandler;
import wiresegal.psionup.common.PsionicUpgrades;
import wiresegal.psionup.common.lib.LibMisc;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFlashRing extends Item implements ISpellContainer {
	public ItemFlashRing() {
		setMaxStackSize(1);
		
		addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "active"), (stack, world, ent) -> containsSpell(stack) ? 1f : 0f);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack held = player.getHeldItem(hand);
		if(!player.isSneaking() && containsSpell(held)) {
			PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
			ItemStack cad = PsiAPI.getPlayerCAD(player);
			boolean did = false;
			if(!cad.isEmpty()) {
				did = ItemCAD.cast(world, player, data, held, cad, (int) (getCostModifier(held) * 15), 25, 0.5f, null);
			}
			return new ActionResult<>(did ? EnumActionResult.SUCCESS : EnumActionResult.PASS, held);
		} else if(player.isSneaking()) {
			if(world.isRemote) {
				player.openGui(PsionicUpgrades.INSTANCE, GuiHandler.GUI_FLASH_RING, world, 0, 0, 0);
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, held);
		}
		
		return new ActionResult<>(EnumActionResult.PASS, held);
	}
	
	//TODO: This 100 magic numbers differs from the 15 up there. Why?
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
		String cost = I18n.translateToLocalFormatted("psimisc.bulletCost", (int) (getCostModifier(stack) * 100));
		tooltip.add(cost);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if(containsSpell(stack)) {
			Spell spell = getSpell(stack);
			
			if(spell != null && !spell.name.isEmpty()) return spell.name;
		}
		
		return super.getItemStackDisplayName(stack);
	}
	
	@Override
	public Spell getSpell(ItemStack stack) {
		return ItemSpellDrive.getSpell(stack);
	}
	
	@Override
	public void setSpell(EntityPlayer entityPlayer, ItemStack stack, Spell spell) {
		ItemSpellDrive.setSpell(stack, spell);
	}
	
	@Override
	public boolean containsSpell(ItemStack stack) {
		Spell s = getSpell(stack);
		return s != null && !s.grid.isEmpty();
	}
	
	@Override
	public void castSpell(ItemStack stack, SpellContext context) {
		context.cspell.safeExecute(context);
	}
	
	@Override
	public double getCostModifier(ItemStack stack) {
		return 5d;
	}
	
	@Override
	public boolean isCADOnlyContainer(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return true; //Because the Flash Ring has shift-click behavior, this will never fire. -wire
	}
	
	@Override
	public boolean hasContainerItem() {
		return true;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return stack.copy();
	}
}
