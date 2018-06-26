package wiresegal.psionup.common.items.component;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADColorizer;
import wiresegal.psionup.common.core.helper.LibLibReplacementItemNBTHelper;
import wiresegal.psionup.common.core.helper.QuatMiscHelpers;
import wiresegal.psionup.common.items.ModItems;
import wiresegal.psionup.common.items.base.ICadComponentAcceptor;
import wiresegal.psionup.common.items.base.ItemComponent;
import wiresegal.psionup.common.lib.LibMisc;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLiquidColorizer extends ItemComponent implements ICADColorizer, ICadComponentAcceptor {
	@SideOnly(Side.CLIENT)
	@Override
	public int getColor(ItemStack stack) {
		int color = getColorFromStack(stack);
		
		if(!stack.isEmpty()) {
			ItemStack inheriting = getInheriting(stack);
			if(!inheriting.isEmpty() && inheriting.getItem() instanceof ICADColorizer) {
				int inheritColor = ((ICADColorizer)inheriting.getItem()).getColor(inheriting);
				
				if(color == Integer.MAX_VALUE) {
					//The user hasn't dyed this colorizer, so just use the exact color of the inherited item.
					color = inheritColor;
				} else {
					//The user did dye their colorizer, so pick a color halfway between.
					//TODO Yoooo throw a sinewave on this, it will look sick
					float[] colorSplit = QuatMiscHelpers.splitColor(color);
					float[] inheritColorSplit = QuatMiscHelpers.splitColor(inheritColor);
					float[] lerpedColor = QuatMiscHelpers.lerpColor(colorSplit, inheritColorSplit, 0.5f);
					color = QuatMiscHelpers.mergeColor(lerpedColor);
				}
			}
		}
		
		return color == Integer.MAX_VALUE ? ICADColorizer.DEFAULT_SPELL_COLOR : color;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
		super.addInformation(stack, world, tooltip, mistake);
		
		if(GuiScreen.isShiftKeyDown()) {
			ItemStack inheriting = getInheriting(stack);
			if(!inheriting.isEmpty()) {
				String translatedPrefix = I18n.translateToLocal(LibMisc.MOD_ID + ".misc.color_inheritance");
				tooltip.add(TextFormatting.GREEN + translatedPrefix + TextFormatting.GRAY + ": " + inheriting.getDisplayName());
			}
			
			if(mistake.isAdvanced()) {
				int color = getColorFromStack(stack);
				if(color != Integer.MAX_VALUE) {
					String formattedNumber = String.format("%06X", color);
					if(formattedNumber.length() > 6) formattedNumber = formattedNumber.substring(formattedNumber.length() - 6);
					String translatedPrefix = I18n.translateToLocal(LibMisc.MOD_ID + ".misc.color");
					tooltip.add(TextFormatting.GREEN + translatedPrefix + TextFormatting.GRAY + ": #" + formattedNumber);
				}
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack held = player.getHeldItem(hand);
		
		if(player.isSneaking()) {
			held = new ItemStack(ModItems.drainedColorizer);
		}
		
		return new ActionResult<>(EnumActionResult.SUCCESS, held);
	}
	
	private static String TAG_COLOR = "Color";
	private static String TAG_INHERITING = "Inheriting";
	
	private static int getColorFromStack(ItemStack stack) {
		return LibLibReplacementItemNBTHelper.getIntegerOrDefault(stack, TAG_COLOR, Integer.MAX_VALUE);
	}
	
	private static ItemStack getInheriting(ItemStack stack) {
		return LibLibReplacementItemNBTHelper.getItemStack(stack, TAG_INHERITING);
	}
	
	private static ItemStack setInheriting(ItemStack stack, ItemStack inheriting) {
		if(inheriting.isEmpty()) {
			LibLibReplacementItemNBTHelper.removeTag(stack, TAG_INHERITING);
		} else {
			LibLibReplacementItemNBTHelper.setItemStack(stack, TAG_INHERITING, inheriting);
		}
		
		return stack;
	}
	
	@NotNull
	@Override
	public ItemStack setPiece(@NotNull ItemStack stack, @NotNull EnumCADComponent type, @NotNull ItemStack piece) {
		return type == EnumCADComponent.DYE ? setInheriting(stack, piece) : stack;
	}
	
	@NotNull
	@Override
	public ItemStack getPiece(@NotNull ItemStack stack, @NotNull EnumCADComponent type) {
		return type == EnumCADComponent.DYE ? getInheriting(stack) : ItemStack.EMPTY;
	}
	
	@Override
	public boolean acceptsPiece(@NotNull ItemStack stack, @NotNull EnumCADComponent type) {
		return type == EnumCADComponent.DYE;
	}
	
	//TODO item color
}
