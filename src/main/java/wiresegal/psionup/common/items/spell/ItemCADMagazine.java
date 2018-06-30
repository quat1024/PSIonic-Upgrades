package wiresegal.psionup.common.items.spell;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.VanillaPacketDispatcher;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.block.tile.TileProgrammer;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.spell.SpellCompiler;
import wiresegal.psionup.client.core.handler.GuiHandler;
import wiresegal.psionup.common.PsionicUpgrades;
import wiresegal.psionup.common.items.base.ICadComponentAcceptor;
import wiresegal.psionup.common.lib.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCADMagazine extends Item implements IPsiAddonTool, ICadComponentAcceptor {
	public ItemCADMagazine() {
		setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);
		//TODO What's this about "example sockets"?
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote && !PsiAPI.getPlayerCAD(player).isEmpty()) {
			player.openGui(PsionicUpgrades.INSTANCE, GuiHandler.GUI_MAGAZINE, world, 0, 0, 0);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		if(!(tile instanceof TileProgrammer)) return EnumActionResult.PASS;
		TileProgrammer programmer = (TileProgrammer) tile;
		
		ItemStack heldStack = player.getHeldItem(hand);
		Spell spell = getSpell(heldStack);
		if(spell == null) return EnumActionResult.PASS;
		
		SpellCompiler compiler = new SpellCompiler(spell);
		if(compiler.isErrored()) return EnumActionResult.PASS; //TODO does this make sense
		
		boolean programmerEnabled = programmer.enabled;
		
		if(world.isRemote) {
			if(!programmerEnabled || programmer.playerLock.isEmpty() || programmer.playerLock.equals(player.getName())) {
				programmer.spell = spell;
				programmer.onSpellChanged();
				world.markBlockRangeForRenderUpdate(programmer.getPos(), programmer.getPos());
				world.playSound(null, pos, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, .5f, 1f);
			}
		} else {
			if(compiler.getCompiledSpell().metadata.stats.getOrDefault(EnumSpellStat.BANDWIDTH, Integer.MAX_VALUE) > getBandwidth(heldStack)) {
				player.sendStatusMessage(QuatMiscHelpers.createErrorTranslation(LibMisc.MOD_ID + ".misc.too_complex_bullet"), false);
			} else {
				if(programmerEnabled && !programmer.playerLock.isEmpty()) {
					if(!programmer.playerLock.equals(player.getName())) {
						player.sendStatusMessage(QuatMiscHelpers.createErrorTranslation("psimisc.notYourProgrammer"), false);
						return EnumActionResult.SUCCESS;
					}
				} else {
					programmer.playerLock = player.getName();
				}
				
				programmer.spell = spell;
				programmer.onSpellChanged();
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(programmer);
				return EnumActionResult.SUCCESS;
			}
		}
		
		return EnumActionResult.PASS;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
		if(GuiScreen.isShiftKeyDown()) {
			ItemStack socket = getSocket(stack);
			if(socket.isEmpty()) return;
			
			String socketName = I18n.translateToLocal(socket.getDisplayName());
			tooltip.add(TextFormatting.GREEN + I18n.translateToLocalFormatted(EnumCADComponent.SOCKET.getName()) + TextFormatting.GRAY + ": " + socketName);
			
			Item socketItem = socket.getItem();
			if(socketItem instanceof ICADComponent) {
				ICADComponent component = (ICADComponent) socketItem;
				
				for(EnumCADStat statType : EnumCADStat.values()) {
					if(statType.getSourceType() == EnumCADComponent.SOCKET) {
						int statValue = component.getCADStatValue(socket, statType);
						String statValueString = statValue == -1 ? "∞" : String.valueOf(statValue);
						String statName = I18n.translateToLocal(statType.getName());
						
						tooltip.add(" " + TextFormatting.AQUA + statName + TextFormatting.GRAY + ": " + statValueString);
					}
				}
			}
			
			int slot = 0;
			while(isSocketSlotAvailable(stack, slot)) {
				ItemStack bullet = getBulletInSocket(stack, slot);
				if(!bullet.isEmpty()) {
					String fmt = TextFormatting.WHITE + (slot == getSelectedSlot(stack) ? TextFormatting.BOLD.toString() : "");
					tooltip.add("| " + fmt + bullet.getDisplayName());
				}
				
				slot++;
			}
		}
	}
	
	//ICadComponentAcceptor overrides
	
	@Nonnull
	@Override
	public ItemStack setPiece(@Nonnull ItemStack stack, @Nonnull EnumCADComponent type, @Nonnull ItemStack piece) {
		if(type != EnumCADComponent.SOCKET) return stack;
		else return setSocket(stack, piece);
	}
	
	@Nonnull
	@Override
	public ItemStack getPiece(@Nonnull ItemStack stack, @Nonnull EnumCADComponent type) {
		if(type != EnumCADComponent.SOCKET) return ItemStack.EMPTY;
		else return getSocket(stack);
	}
	
	@Override
	public boolean acceptsPiece(@Nonnull ItemStack stack, @Nonnull EnumCADComponent type) {
		return type == EnumCADComponent.SOCKET;
	}
	
	//IPsiAddonTool overrides
	
	@Override
	public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
		return slot < getSocketSlots(stack);
	}
	
	@Override
	public void setSpell(EntityPlayer player, ItemStack stack, Spell spell) {
		int slot = getSelectedSlot(stack);
		ItemStack spellBullet = getBulletInSocket(stack, slot);
		SpellCompiler compiler = new SpellCompiler(spell);
		
		if(compiler.isErrored()) return; //TODO does that make sense here, the original doesnt do it
		
		if(compiler.getCompiledSpell().metadata.stats.getOrDefault(EnumSpellStat.BANDWIDTH, Integer.MAX_VALUE) > getBandwidth(stack)) {
			if(!player.world.isRemote) {
				player.sendStatusMessage(QuatMiscHelpers.createErrorTranslation(LibMisc.MOD_ID + ".misc.too_complex"), false);
			}
		} else if(!spellBullet.isEmpty() && spellBullet.getItem() instanceof ISpellSettable) {
			//TODO: won't this let people work around the spell bandwidth restriction?
			((ISpellSettable) spellBullet.getItem()).setSpell(player, spellBullet, spell);
			setBulletInSocket(stack, slot, spellBullet);
		}
	}
	
	//helpers
	
	public static ItemStack getSocket(ItemStack stack) {
		NBTTagCompound nbt = ItemNBTHelpers.getCompound(stack, "Socket");
		if(nbt == null) return new ItemStack(ModItems.cadSocket);
		else return new ItemStack(nbt);
	}
	
	public static ItemStack setSocket(ItemStack stack, ItemStack socket) {
		if(socket.isEmpty()) ItemNBTHelpers.removeTag(stack, "Socket");
		else ItemNBTHelpers.setItemStack(stack, "Socket", socket);
		return stack;
	}
	
	public static int getSocketSlots(ItemStack stack) {
		ItemStack socket = getSocket(stack);
		Item socketItem = socket.getItem();
		if(socketItem instanceof ICADComponent) {
			return ((ICADComponent)socketItem).getCADStatValue(socket, EnumCADStat.SOCKETS);
		} else return 0;
	}
	
	public static int getBandwidth(ItemStack stack) {
		ItemStack socket = getSocket(stack);
		Item socketItem = socket.getItem();
		if(socketItem instanceof ICADComponent) {
			return ((ICADComponent)socketItem).getCADStatValue(socket, EnumCADStat.BANDWIDTH);
		} else return 0;
	}
	
	public Spell getSpell(ItemStack stack) {
		return ItemSpellDrive.getSpell(getBulletInSocket(stack, getSelectedSlot(stack)));
	}
}
