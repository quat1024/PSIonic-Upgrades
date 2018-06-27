package wiresegal.psionup.client.render.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class LayerGlowingWire implements LayerRenderer<AbstractClientPlayer> {
	public LayerGlowingWire(RenderPlayer renderPlayer) {
		this.renderPlayer = renderPlayer;
	}
	
	RenderPlayer renderPlayer;
	
	@Override
	public void doRenderLayer(AbstractClientPlayer abstractClientPlayer, float v, float v1, float v2, float v3, float v4, float v5, float v6) {
		//TODO
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
	//helpers
	
	private static final UUID wireUUID = UUID.fromString("458391f5-6303-4649-b416-e4c0d18f837a");
	private static final UUID quatUUID = UUID.fromString("873dea16-d058-4343-861c-f62c21da124b");
	
	private static boolean isSpecialSnowflake(EntityPlayer player) {
		UUID playerUUID = player.getUniqueID();
		return playerUUID.equals(wireUUID) || playerUUID.equals(quatUUID);
	}
}
