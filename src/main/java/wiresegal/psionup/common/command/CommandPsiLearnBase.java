package wiresegal.psionup.common.command;

import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import vazkii.psi.api.PsiAPI;
import wiresegal.psionup.common.lib.LibMisc;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandPsiLearnBase extends CommandBase {
	static final List<String> allGroupNames;
	static final String level0Name = "psidust";
	
	static {
		//TODO static init feels too early/fragile/classloady but a named init method feels too overkill
		allGroupNames = new ArrayList<>();
		allGroupNames.addAll(PsiAPI.groupsForName.keySet());
		allGroupNames.add(level0Name);
	}
	
	String getLocalizationKey() {
		return LibMisc.MOD_ID + ".command." + getName();
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return getLocalizationKey() + ".usage";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0 || args.length > 3) {
			throw new WrongUsageException(getLocalizationKey() + ".usage");
		}
		
		Entity learningEntity;
		if(args.length == 2) {
			learningEntity = CommandBase.getEntity(server, sender, args[1]);
		} else {
			learningEntity = sender.getCommandSenderEntity();
		}
		
		if(learningEntity == null) {
			throw new CommandException(getLocalizationKey() + ".error.not_found");
		}
		
		if(!(learningEntity instanceof EntityPlayer)) {
			throw new CommandException(getLocalizationKey() + ".error.not_player", learningEntity.getDisplayName());
		}
		
		EntityPlayer learningPlayer = (EntityPlayer) learningEntity;
		String groupName = args[0];
		
		if(groupName.equals("*")) {
			performAll(learningPlayer, sender);
		} else {
			performOne(learningPlayer, groupName, sender);
		}
	}
	
	abstract void performAll(EntityPlayer learningPlayer, ICommandSender sender) throws CommandException;
	abstract void performOne(EntityPlayer learningPlayer, String groupName, ICommandSender sender) throws CommandException;
}
