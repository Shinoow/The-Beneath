package com.shinoow.beneath.client.render.entity;

import com.shinoow.beneath.common.entity.EntityShadow;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderShadow extends RenderBiped<EntityShadow> {
	private static final ResourceLocation mobTexture = new ResourceLocation("beneath","textures/entity/shadow.png");

	public RenderShadow(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelBiped(), 0F);

	}

	@Override
	protected void renderModel(EntityShadow entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
	{
		bindEntityTexture(entitylivingbaseIn);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
		GlStateManager.disableBlend();
	}

	@Override
	protected boolean setBrightness(EntityShadow entitylivingbaseIn, float partialTicks, boolean combineTextures)
	{
		return false;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityShadow entity) {

		return mobTexture;
	}
}
