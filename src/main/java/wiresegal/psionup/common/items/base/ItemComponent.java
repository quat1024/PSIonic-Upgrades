package wiresegal.psionup.common.items.base;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.*;
import wiresegal.psionup.common.lib.LibMisc;

import javax.annotation.Nullable;
import java.util.*;

public abstract class ItemComponent extends Item implements ICADComponent {
	public ItemComponent() {
		setMaxStackSize(1);
		registerStats();
	}
	
	//Porting note: I removed metadata support -quat
	private Map<EnumCADStat, Integer> stats = new HashMap<>();
	
	protected void registerStats() {
		//No-op by default
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
		if(!GuiScreen.isShiftKeyDown()) return;
		
		addTooltipTags(tooltip);
		
		EnumCADComponent componentType = getComponentType(stack);
		String componentName = I18n.translateToLocal(componentType.getName());
		tooltip.add(I18n.translateToLocalFormatted("psimisc.componentType", componentName));
		
		for(EnumCADStat cadStat : EnumCADStat.values()) {
			if(cadStat.getSourceType() == componentType) {
				int statValue = getCADStatValue(stack, cadStat);
				String statValueString = statValue == -1 ? "∞" : String.valueOf(statValue);
				String statName = I18n.translateToLocal(cadStat.getName());
				tooltip.add(" " + TextFormatting.AQUA + statName + TextFormatting.GRAY + ": " + statValueString);
			}
		}
	}
	
	protected void addTooltipTags(List<String> tooltip) {
		//No-op
	}
	
	protected void addTooltipTag(boolean positiveEffect, List<String> tooltip, String descriptionTranslationKey, Object... descriptionFormatArgs) {
		String nameFormatted = I18n.translateToLocal(LibMisc.MOD_ID + ".cadstat." + (positiveEffect ? "extra" : "downside"));
		
		if(descriptionFormatArgs == null) descriptionFormatArgs = new String[0];
		String descriptionFormatted = I18n.translateToLocalFormatted(descriptionTranslationKey, descriptionFormatArgs);
		
		TextFormatting color = positiveEffect ? TextFormatting.AQUA : TextFormatting.RED;
		
		tooltip.add(" " + color + nameFormatted + ": " + TextFormatting.GRAY + descriptionFormatted);
	}
	
	protected void addStat(EnumCADStat stat, int value) {
		stats.put(stat, value);
	}
	
	@Override
	public int getCADStatValue(ItemStack stack, EnumCADStat stat) {
		return stats.getOrDefault(stat, 0);
	}
}
