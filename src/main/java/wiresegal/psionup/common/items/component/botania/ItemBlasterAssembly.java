package wiresegal.psionup.common.items.component.botania;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.spell.*;
import wiresegal.psionup.api.enabling.ITrickEnablerComponent;
import wiresegal.psionup.api.enabling.botania.*;
import wiresegal.psionup.common.core.helper.QuatMiscHelpers;
import wiresegal.psionup.common.items.base.ItemComponent;
import wiresegal.psionup.common.lib.LibMisc;

import java.util.List;

public class ItemBlasterAssembly extends ItemComponent implements IBlasterComponent {
	public ItemBlasterAssembly() {
		//TODO find a home for those recipes.
		MinecraftForge.EVENT_BUS.register(this.getClass());
		
		vazkii.psi.common.item.base.ModItems.cad.addPropertyOverride(new ResourceLocation(LibMisc.MOD_ID, "clip"), ((stack, world, ent) -> ItemManaGun.hasClip(stack) ? 1f : 0f));
	}
	
	//TODO this gay
	@SideOnly(Side.CLIENT)
	@Override
	public ModelResourceLocation getCADModel(ItemStack itemStack, ItemStack itemStack1) {
		return new ModelResourceLocation("minecraft:stone"); //TODO
	}
	
	@Override
	protected void addTooltipTags(List<String> tooltip) {
		//TODO my modified addtooltip is a bit different
		addTooltipTag(true, tooltip, LibMisc.MOD_ID + ".requirement.mana_cad");
	}
	
	@Override
	protected void registerStats() {
		addStat(EnumCADStat.EFFICIENCY, 80);
		addStat(EnumCADStat.POTENCY, 250);
	}
	
	@Override
	public EnableResult enablePiece(EntityPlayer player, ItemStack component, ItemStack cad, SpellContext context, Spell spell, int x, int y) {
		boolean isElven = ItemManaGun.hasClip(cad);
		EnumManaTier cadTier = isElven ? EnumManaTier.ALFHEIM : EnumManaTier.BASE;
		
		SpellPiece piece = spell.grid.gridData[x][y];
		
		if(piece instanceof IManaTrick) {
			IManaTrick manaPiece = (IManaTrick) piece;
			if(EnumManaTier.allowed(cadTier, manaPiece.tier())) {
				int manaDrain = manaPiece.manaDrain(context, x, y);
				return ITrickEnablerComponent.EnableResult.fromBoolean(ManaItemHandler.requestManaExact(cad, context.caster, manaDrain, true));
			}
		}
		
		return EnableResult.NOT_ENABLED;
	}
	
	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e) {
		//TODO
	}
	
	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.RightClickItem e) {
		if(e.getEntityPlayer().isSneaking()) {
			ItemStack heldStack = e.getItemStack();
			EnumHand hand = e.getHand();
			World world = e.getWorld();
			
			if(!heldStack.isEmpty() && heldStack.getItem() instanceof ICAD) {
				ICAD icad = (ICAD) heldStack.getItem();
				boolean hasBlasterAssembly = icad.getComponentInSlot(heldStack, EnumCADComponent.ASSEMBLY).getItem() instanceof IBlasterComponent;
				if(hasBlasterAssembly && ItemManaGun.hasClip(heldStack)) {
					ItemManaGun.rotatePos(heldStack);
					
					QuatMiscHelpers.emitSoundFromEntity(world, e.getEntityPlayer(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, .6f, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
					
					if(world.isRemote) e.getEntityPlayer().swingArm(hand);
					
					ItemsRemainingRenderHandler.set(ItemManaGun.getLens(heldStack), -2);
					
					e.setCanceled(true);
				}
			}
		}
	}
}
