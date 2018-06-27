package wiresegal.psionup.common.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import wiresegal.psionup.common.lib.PlayerDataHelpers;

public class CommandPsiUnlearn extends CommandPsiLearnBase {
	@Override
	public String getName() {
		return "psi-unlearn";
	}
	
	void performAll(EntityPlayer learner, ICommandSender sender) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(learner);
		
		for(String groupName : PlayerDataHelpers.allGroupNames) {
			if(PlayerDataHelpers.hasGroup(data, groupName)) {
				PlayerDataHelpers.lockGroup(data, groupName);
				data.lastSpellGroup = "";
			}
		}
		
		CommandBase.notifyCommandListener(sender, this, getLocalizationKey() + ".success.all", learner.getDisplayName());
		
		PlayerDataHelpers.sync(learner);
	}
	
	void performOne(EntityPlayer learner, String groupToLock, ICommandSender sender) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(learner);
		
		if(groupToLock.equals(PlayerDataHelpers.level0Name)) {
			PlayerDataHelpers.lockGroup(data, groupToLock);
			CommandBase.notifyCommandListener(sender, this, getLocalizationKey() + ".success.one", learner.getDisplayName(), PlayerDataHelpers.prettyPrintGroupName(groupToLock));
			return;
		}
		
		if(PsiAPI.groupsForName.containsKey(groupToLock) && data.isPieceGroupUnlocked(groupToLock)) {
			PlayerDataHelpers.lockGroup(data, groupToLock);
			CommandBase.notifyCommandListener(sender, this, getLocalizationKey() + ".success.one", learner.getDisplayName(), PlayerDataHelpers.prettyPrintGroupName(groupToLock));
		}
	}
}
