package wiresegal.psionup.client.core;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wiresegal.psionup.client.core.handler.HUDHandler;
import wiresegal.psionup.client.render.entity.ExosuitGlowLayer;
import wiresegal.psionup.client.render.entity.LayerGlowingWire;
import wiresegal.psionup.client.render.tile.RenderTileCADCase;
import wiresegal.psionup.common.block.BlockCADCase;
import wiresegal.psionup.common.block.ModBlocks;
import wiresegal.psionup.common.block.tile.TileCADCase;
import wiresegal.psionup.common.core.CommonProxy;
import wiresegal.psionup.common.items.ModItems;

import java.util.*;
import java.util.function.*;

public class ClientProxy extends CommonProxy {
	@Override
	public void pre(FMLPreInitializationEvent e) {
		super.pre(e);
		
		//Don't use SubscribeEvent because I've had some problems when combining it with SideOnly in the past
		MinecraftForge.EVENT_BUS.register(HUDHandler.class);
		MinecraftForge.EVENT_BUS.register(ClientEvents.class);
	}
	
	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileCADCase.class, new RenderTileCADCase());
		
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		addRenderLayers(skinMap.get("default"));
		addRenderLayers(skinMap.get("slim"));
	}
	
	private void addRenderLayers(RenderPlayer render) {
		render.addLayer(new ExosuitGlowLayer(render));
		render.addLayer(new LayerGlowingWire(render));
		
		//TODO the meme yrsegal elytra handler
	}
	
	@Override
	public void post(FMLPostInitializationEvent e) {
		super.post(e);
	}
	
	public static class ClientEvents {
		@SubscribeEvent
		public static void blockColors(ColorHandlerEvent.Block e) {
			BlockColors bc = e.getBlockColors();
			
			bc.registerBlockColorHandler(((iBlockState, iBlockAccess, blockPos, i) -> {
				return 0; //TODO
			}), ModBlocks.brightPlate, ModBlocks.darkPlate);
		}
		
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {
			ModelLoader.setCustomStateMapper(ModBlocks.conjuredPulsar, (b) -> Collections.emptyMap());
			ModelLoader.setCustomStateMapper(ModBlocks.conjuredStar, (b) -> Collections.emptyMap());
			
			IStateMapper ignoringAllMapper = new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation(state.getBlock().getRegistryName(), "normal");
				}
			};
			
			ModelLoader.setCustomStateMapper(ModBlocks.brightPlate, ignoringAllMapper);
			ModelLoader.setCustomStateMapper(ModBlocks.darkPlate, ignoringAllMapper);
			
			ModelLoader.setCustomStateMapper(ModBlocks.cadCase, new StateMap.Builder().ignore(BlockCADCase.COLOR).build());
			
			setDefaultModel(ModItems.liquidColorizer);
			setDefaultModel(ModItems.drainedColorizer);
			setDefaultModel(ModItems.inlineCaster);
			setDefaultModel(ModItems.spellMagazine);
			setDefaultModel(ModItems.wideBandSocket);
			
			setDefaultModel(ModItems.ebonyHelmet);
			setDefaultModel(ModItems.ebonyChest);
			setDefaultModel(ModItems.ebonyLegs);
			setDefaultModel(ModItems.ebonyBoots);
			setDefaultModel(ModItems.ivoryHelmet);
			setDefaultModel(ModItems.ivoryChest);
			setDefaultModel(ModItems.ivoryLegs);
			setDefaultModel(ModItems.ivoryBoots);
			setDefaultModel(ModItems.ebonyPickaxe);
			setDefaultModel(ModItems.ebonyShovel);
			setDefaultModel(ModItems.ebonyAxe);
			setDefaultModel(ModItems.ebonySword);
			setDefaultModel(ModItems.ivoryPickaxe);
			setDefaultModel(ModItems.ivoryShovel);
			setDefaultModel(ModItems.ivoryAxe);
			setDefaultModel(ModItems.ivorySword);
			
			setDefaultModel(ModItems.flashRing);
			setDefaultModel(ModItems.bioticSensor);
			setDefaultModel(ModItems.gaussRifle);
			setDefaultModel(ModItems.gaussBullet);
			setDefaultModel(ModItems.unstableBattery);
			setDefaultModel(ModItems.twinflowBattery);
			
			set16ColorsModel(ModItems.darkPlateItem);
			set16ColorsModel(ModItems.brightPlateItem);
			
			for(int i = 0; i < 16; i++) {
				setDefaultModel(ModItems.cadCaseItem, i);
			}
		}
		
		private static void setDefaultModel(Item i) {
			setDefaultModel(i, 0);
		}
		
		private static void setDefaultModel(Item i, int damage) {
			ModelResourceLocation mrl = new ModelResourceLocation(i.getRegistryName(), "inventory");
			ModelLoader.setCustomModelResourceLocation(i, damage, mrl);
		}
		
		private static void set16ColorsModel(Item i) {
			for(int j=0; j < 16; j++) {
				ResourceLocation res = i.getRegistryName();
				String suffixedPath = res.getResourcePath() + "_" + EnumDyeColor.byMetadata(j).getName();
				ResourceLocation res2 = new ResourceLocation(res.getResourceDomain(), suffixedPath);
				ModelResourceLocation mrl = new ModelResourceLocation(res2, "inventory");
				ModelLoader.setCustomModelResourceLocation(i, j, mrl);
			}
		}
	}
}
