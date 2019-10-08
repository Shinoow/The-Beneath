package com.shinoow.beneath.client.render.entity.layers;

import java.util.UUID;

import com.shinoow.beneath.client.model.player.ModelStarSpawnPlayer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;

public class LayerStarSpawnTentacles implements LayerRenderer<EntityPlayer> {

	private ModelStarSpawnPlayer model = new ModelStarSpawnPlayer();
	private final RenderPlayer render;
	private final ResourceLocation texture = new ResourceLocation("beneath:textures/entity/tentacles.png");

	public LayerStarSpawnTentacles(RenderPlayer render){
		this.render = render;
	}

	@Override
	public void doRenderLayer(EntityPlayer player, float f, float f1, float partialTicks, float f2, float f3, float f4, float scale){
		if(checkPlayer(player) && !player.isInvisible()){

			render.bindTexture(texture);

			for (int j = 0; j < 1; ++j) {
				float f10 = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
				float f11 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
				GlStateManager.pushMatrix();
				if (player.isSneaking())
					GlStateManager.translate(0.0F, 0.24F, 0.0F);
				GlStateManager.rotate(f10, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(f11, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0, -0.22F, 0);
				model.renderTentacles(0.0625F, player);
				GlStateManager.popMatrix();
			}
		}
	}

	private boolean checkPlayer(EntityPlayer player){
		if((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment"))
			return player.getName().equals("shinoow") || player.getName().equals("Oblivionaire");
		else return player.getUniqueID().equals(UUID.fromString("a5d8abca-0979-4bb0-825a-f1ccda0b350b")) || player.getUniqueID().equals(UUID.fromString("08f3211c-d425-47fd-afd8-f0e7f94152c4"));
	}

	@Override
	public boolean shouldCombineTextures() {

		return false;
	}
}