package wiresegal.psionup.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wiresegal.psionup.common.block.ModBlocks;
import wiresegal.psionup.common.command.CommandPsiLearn;
import wiresegal.psionup.common.command.CommandPsiUnlearn;
import wiresegal.psionup.common.core.CommonProxy;
import wiresegal.psionup.common.core.PsionicSoundEvents;
import wiresegal.psionup.common.entity.ModEntities;
import wiresegal.psionup.common.items.ModItems;
import wiresegal.psionup.common.lib.LibMisc;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES, acceptedMinecraftVersions = LibMisc.VERSIONS)
public class PsionicUpgrades {
	@Mod.Instance
	public static PsionicUpgrades INSTANCE = null;
	
	@SidedProxy(serverSide = LibMisc.PROXY_COMMON, clientSide = LibMisc.PROXY_CLIENT)
	public static CommonProxy PROXY;
	
	public static final Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
		PROXY.pre(e);
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		PROXY.init(e);
	}
	
	@Mod.EventHandler
	public static void postinit(FMLPostInitializationEvent e) {
		PROXY.post(e);
	}
	
	@Mod.EventHandler
	public static void serverStarting(FMLServerStartingEvent e) {
		e.registerServerCommand(new CommandPsiLearn());
		e.registerServerCommand(new CommandPsiUnlearn());
	}
	
	@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			ModBlocks.register(e.getRegistry());
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			ModItems.register(e.getRegistry());
		}
		
		@SubscribeEvent
		public static void entities(RegistryEvent.Register<EntityEntry> e) {
			ModEntities.register(e.getRegistry());
		}
		
		@SubscribeEvent
		public static void sounds(RegistryEvent.Register<SoundEvent> e) {
			PsionicSoundEvents.registerSounds(e.getRegistry());
		}
	}
	
	@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Side.CLIENT)
	public static class ClientEvents {
		@SubscribeEvent
		public static void blockColors(ColorHandlerEvent.Block e) {
			ModBlocks.registerBlockColors(e.getBlockColors());
		}
	}
}
