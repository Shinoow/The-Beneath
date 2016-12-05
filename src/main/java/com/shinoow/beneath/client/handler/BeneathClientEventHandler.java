package com.shinoow.beneath.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BeneathClientEventHandler {

	private static int timer = 0;

	public static void startTimer(){
		timer = 60;
	}

	@SubscribeEvent
	public void overlay(RenderGameOverlayEvent.Pre event){
		if(event.getType() != ElementType.CROSSHAIRS) return;
		if(timer == 0) return;
		final ResourceLocation hand = new ResourceLocation("beneath", "textures/misc/shadow_hand.png");

		if(timer > 0){
			if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
				GlStateManager.disableDepth();
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

				Minecraft.getMinecraft().renderEngine.bindTexture(hand);
				ScaledResolution scaledRes = event.getResolution();
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vertexbuffer = tessellator.getBuffer();

				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
				vertexbuffer.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
				vertexbuffer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
				vertexbuffer.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
				vertexbuffer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
				tessellator.draw();
				GlStateManager.depthMask(true);
				GlStateManager.enableDepth();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}

			timer--;
		}
	}
}
