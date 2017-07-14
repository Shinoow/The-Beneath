package com.shinoow.beneath.client.render.entity;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import com.shinoow.beneath.client.model.entity.ModelHand;
import com.shinoow.beneath.common.entity.EntityHand;

public class RenderHand extends Render<EntityHand> {

	ModelHand mainModel = new ModelHand();

	public RenderHand(RenderManager renderManager) {
		super(renderManager);
	}

	protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks)
	{
		float f;

		for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F)
			;

		while (f >= 180.0F)
			f -= 360.0F;

		return prevYawOffset + partialTicks * f;
	}

	@Override
	public void doRender(EntityHand entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		try
		{
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.rotate(180.0F - interpolateRotation(entity.prevRotationYaw, entity.rotationYaw, partialTicks), 0.0F, 1.0F, 0.0F);

			GlStateManager.scale(0.1F, 0.1F, 0.1F);
			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(-1.0F, -1.0F, 1.0F);
			GlStateManager.translate(0.0F, -1.501F, 0.0F);

			GlStateManager.enableAlpha();
			mainModel.setRotationAngles(0, 0, 0, entityYaw, 0, 1, entity);

			renderModel(entity, 0, 0, 0, entityYaw, 0, 1);

			GlStateManager.depthMask(true);

			GlStateManager.disableRescaleNormal();
		}
		catch (Exception exception) {}

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	protected void renderModel(EntityHand entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
	{
		bindEntityTexture(entitylivingbaseIn);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		GlStateManager.disableBlend();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityHand entity) {
		return new ResourceLocation("beneath","textures/entity/hand.png");
	}
}
