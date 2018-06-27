package wiresegal.psionup.common.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import wiresegal.psionup.common.lib.PlayerDataHelpers;

public class CommandPsiLearn extends CommandPsiLearnBase {
	@Override
	public String getName() {
		return "psi-learn";
	}
	
	void performOne(EntityPlayer learner, String groupToUnlock, ICommandSender sender) throws CommandException {
		if(!PlayerDataHelpers.allGroupNames.contains(groupToUnlock)) {
			throw new CommandException(getLocalizationKey() + ".error.not_group", groupToUnlock);
		}
		
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(learner);
		
		if(groupToUnlock.equals(PlayerDataHelpers.level0Name)) {
			PlayerDataHelpers.unlockGroupForFree(data, PlayerDataHelpers.level0Name);
			CommandBase.notifyCommandListener(sender, this, getLocalizationKey() + ".success.one", learner.getDisplayName(), PlayerDataHelpers.prettyPrintGroupName(groupToUnlock));
			return;
		}
		
		if(data.spellGroupsUnlocked.contains(groupToUnlock)) {
			throw new CommandException(getLocalizationKey() + ".already_has", learner.getDisplayName(), PlayerDataHelpers.prettyPrintGroupName(groupToUnlock));
		}
		
		if(!PlayerDataHelpers.hasGroup(data, groupToUnlock)) {
			PieceGroup group = PsiAPI.groupsForName.get(groupToUnlock);
			for(String parentGroup : group.requirements) {
				if(!data.isPieceGroupUnlocked(parentGroup)) {
					performOne(learner, parentGroup, sender);
				}
			}
			
			PlayerDataHelpers.unlockGroupForFree(data, groupToUnlock);
		}
		
		CommandBase.notifyCommandListener(sender, this, getLocalizationKey() + ".success.one", learner.getDisplayName(), PlayerDataHelpers.prettyPrintGroupName(groupToUnlock));
		
		PlayerDataHelpers.sync(learner);
	}
	
	void performAll(EntityPlayer learner, ICommandSender sender) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(learner);
		
		for(String groupName : PlayerDataHelpers.allGroupNames) {
			if(!PlayerDataHelpers.hasGroup(data, groupName)) {
				PlayerDataHelpers.unlockGroupForFree(data, groupName);
				data.lastSpellGroup = "";
			}
		}
		
		CommandBase.notifyCommandListener(sender, this, getLocalizationKey() + ".success.all", learner.getDisplayName());
		
		PlayerDataHelpers.sync(learner);
	}
}
