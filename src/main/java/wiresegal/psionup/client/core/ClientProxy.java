package wiresegal.psionup.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import wiresegal.psionup.client.core.handler.HUDHandler;
import wiresegal.psionup.client.render.entity.ExosuitGlowLayer;
import wiresegal.psionup.client.render.entity.LayerGlowingWire;
import wiresegal.psionup.client.render.tile.RenderTileCADCase;
import wiresegal.psionup.common.block.tile.TileCADCase;
import wiresegal.psionup.common.core.CommonProxy;

import java.util.*;

public class ClientProxy extends CommonProxy {
	@Override
	public void pre(FMLPreInitializationEvent e) {
		super.pre(e);
		
		//Don't use SubscribeEvent because I've had some problems when combining it with SideOnly in the past
		MinecraftForge.EVENT_BUS.register(HUDHandler.class);
		
		//HUDHandler.init();
		
		/*
		 GlowingHandler.registerCustomGlowHandler(PsiItems.cad, {
            _, model -> IGlowingItem.Helper.wrapperBake(model, false, 1, 2)
        }) { _, _ -> true }
		 */
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
		
		//This is a fragile-feeling method, and the sole purpose is to make a funny meme for yrsegal.
		//TODO Remove?
		ListIterator<LayerRenderer<AbstractClientPlayer>> renderator = PsionicClientMethodHandles.getRenderLayers(render).listIterator();
		while(renderator.hasNext()) {
			LayerRenderer<? extends AbstractClientPlayer> layer = renderator.next();
			if(layer instanceof LayerElytra) {
				renderator.remove();
				renderator.add(new LayerWireOccludeElytra((LayerElytra) layer));
			}
		}
	}
	
	static class LayerWireOccludeElytra implements LayerRenderer<AbstractClientPlayer> {
		LayerElytra original;
		static final UUID WIRE_UUID = UUID.fromString("458391f5-6303-4649-b416-e4c0d18f837a");
		
		public LayerWireOccludeElytra(LayerElytra original) {
			this.original = original;
		}
		
		@Override
		public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			if(!player.getUniqueID().equals(WIRE_UUID) || !player.isWearing(EnumPlayerModelParts.CAPE)) {
				original.doRenderLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			}
		}
		
		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}
	
	@Override
	public void post(FMLPostInitializationEvent e) {
		super.post(e);
	}
}
