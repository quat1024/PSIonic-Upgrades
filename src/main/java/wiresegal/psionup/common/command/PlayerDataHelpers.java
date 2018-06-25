package wiresegal.psionup.common.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageDataSync;
import wiresegal.psionup.common.lib.LibMisc;

public class PlayerDataHelpers {
	public static boolean hasGroup(PlayerDataHandler.PlayerData data, String groupName) {
		if(groupName.equals(CommandPsiLearn.level0Name)) return data.level > 0;
		else return data.isPieceGroupUnlocked(groupName);
	}
	
	public static void unlockGroupForFree(PlayerDataHandler.PlayerData data, String groupName) {
		if(groupName.equals(CommandPsiLearn.level0Name)) {
			if(data.level == 0) {
				data.level++;
				data.levelPoints++;
				data.lastSpellGroup = "";
				data.save();
			}
		} else {
			data.spellGroupsUnlocked.add(groupName);
			data.lastSpellGroup = groupName;
			data.level++;
			data.save();
		}
	}
	
	public static void lockGroup(PlayerDataHandler.PlayerData data, String groupName) {
		if(hasGroup(data, groupName)) {
			if(groupName.equals(CommandPsiLearn.level0Name)) {
				data.level = 0;
				data.lastSpellGroup = "";
				data.levelPoints = Math.min(0, data.levelPoints - 1);
			} else {
				data.spellGroupsUnlocked.remove(groupName);
				if(data.lastSpellGroup.equals(groupName)) {
					data.lastSpellGroup = "";
				}
				data.level--;
			}
			
			data.save();
		}
	}
	
	public static ITextComponent prettyPrintGroupName(String groupName) {
		String translationKey;
		int level;
		
		if(groupName.equals(CommandPsiLearn.level0Name)) {
			translationKey = LibMisc.MOD_ID + ".misc.psidust";
			level = 0;
		} else {
			PieceGroup group = PsiAPI.groupsForName.get(groupName);
			if(group == null) {
				TextComponentString errorComponent = new TextComponentString("ERROR !");
				errorComponent.getStyle().setColor(TextFormatting.RED);
				
				TextComponentTranslation errorDetailsComponent = new TextComponentTranslation(LibMisc.MOD_ID + ".command.error.unable_to_print_component");
				
				errorComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, errorDetailsComponent));
				
				return errorComponent;
			}
			translationKey = group.getUnlocalizedName();
			level = group.levelRequirement;
		}
		
		TextComponentTranslation nameComponent = new TextComponentTranslation(translationKey);
		nameComponent.getStyle().setColor(TextFormatting.AQUA);
		//TODO look into that "siblings" thing. I changed how this works to avoid localizing on the server, but that means I lose the square brackets around the name, which looks quite nice tbh
		
		TextComponentTranslation hoverComponent = new TextComponentTranslation("psimisc.levelDisplay", level);
		
		nameComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
		
		return nameComponent;
	}
	
	public static void sync(EntityPlayer ent) {
		if(ent instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) ent;
			NetworkHandler.INSTANCE.sendTo(new MessageDataSync(PlayerDataHandler.get(playerMP)), playerMP);
		}
	}
}
