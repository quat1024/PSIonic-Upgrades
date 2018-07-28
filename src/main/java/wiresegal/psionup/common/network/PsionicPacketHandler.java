package wiresegal.psionup.common.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import wiresegal.psionup.client.core.handler.GuiHandler;
import wiresegal.psionup.common.PsionicUpgrades;
import wiresegal.psionup.common.lib.LibMisc;

public class PsionicPacketHandler {
	public static SimpleNetworkWrapper NET;
	
	public static void initPackets() {
		NET = NetworkRegistry.INSTANCE.newSimpleChannel(LibMisc.MOD_ID);
		
		int id = 0;
		NET.registerMessage(MessageFlashSync.Handler.class, MessageFlashSync.class, id++, Side.SERVER);
		NET.registerMessage(MessageParticleTrail.Handler.class, MessageParticleTrail.class, id++, Side.CLIENT);
		NET.registerMessage(MessageSparkleSphere.Handler.class, MessageSparkleSphere.class, id++, Side.CLIENT);
		NET.registerMessage(MessageSpamlessChat.Handler.class, MessageSpamlessChat.class, id++, Side.CLIENT);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(PsionicUpgrades.INSTANCE, new GuiHandler());
	}
	
	public static void sendToAllWithinRange(IMessage message, World world, BlockPos point, double range) {
		NET.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), point.getX() + .5, point.getY() + .5, point.getZ() + .5, range));
	}
	
	public static void sendToAllNear(IMessage message, World world, BlockPos point) {
		sendToAllWithinRange(message, world, point, 128);
	}
	
	public static void sendTo(IMessage message, EntityPlayerMP pmp) {
		NET.sendTo(message, pmp);
	}
	
	public static void sendToServer(IMessage message) {
		NET.sendToServer(message);
	}
}
