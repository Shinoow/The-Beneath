package com.shinoow.beneath.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHand extends ModelBase
{
	//fields
	ModelRenderer palm1;
	ModelRenderer palm2;
	ModelRenderer palm3;
	ModelRenderer thumb1;
	ModelRenderer thumb2;
	ModelRenderer index1;
	ModelRenderer index2;
	ModelRenderer index3;
	ModelRenderer middle1;
	ModelRenderer middle2;
	ModelRenderer middle3;
	ModelRenderer ring1;
	ModelRenderer ring2;
	ModelRenderer ring3;
	ModelRenderer little1;
	ModelRenderer little2;
	ModelRenderer little3;

	public ModelHand()
	{
		textureWidth = 64;
		textureHeight = 32;

		palm1 = new ModelRenderer(this, 0, 0);
		palm1.addBox(0F, 0F, 0F, 5, 5, 1);
		palm1.setRotationPoint(-1F, 0F, 0F);
		palm1.setTextureSize(64, 32);
		palm1.mirror = true;
		setRotation(palm1, 0F, 0F, 0F);
		palm2 = new ModelRenderer(this, 0, 0);
		palm2.addBox(0F, 0F, 0F, 2, 2, 1);
		palm2.setRotationPoint(4F, 1F, 0F);
		palm2.setTextureSize(64, 32);
		palm2.mirror = true;
		setRotation(palm2, 0F, 0.2617994F, 0F);
		palm3 = new ModelRenderer(this, 0, 0);
		palm3.addBox(0F, 0F, 0F, 1, 2, 1);
		palm3.setRotationPoint(4F, 3F, 0F);
		palm3.setTextureSize(64, 32);
		palm3.mirror = true;
		setRotation(palm3, 0F, 0.2617994F, 0F);
		thumb1 = new ModelRenderer(this, 0, 0);
		thumb1.addBox(0F, -2F, 0F, 1, 2, 1);
		thumb1.setRotationPoint(5F, 1F, -0.2F);
		thumb1.setTextureSize(64, 32);
		thumb1.mirror = true;
		setRotation(thumb1, 0.2617994F, 0.2617994F, 0F);
		thumb2 = new ModelRenderer(this, 0, 0);
		thumb2.addBox(0F, -2F, 0F, 1, 2, 1);
		thumb2.setRotationPoint(4.9F, -0.9F, -0.7F);
		thumb2.setTextureSize(64, 32);
		thumb2.mirror = true;
		setRotation(thumb2, 0.4363323F, 0.2617994F, 0F);
		index1 = new ModelRenderer(this, 0, 0);
		index1.addBox(0F, -2F, 0F, 1, 2, 1);
		index1.setRotationPoint(3F, 0F, 0F);
		index1.setTextureSize(64, 32);
		index1.mirror = true;
		setRotation(index1, 0.2617994F, 0F, 0F);
		index2 = new ModelRenderer(this, 0, 0);
		index2.addBox(0F, -2F, 0F, 1, 2, 1);
		index2.setRotationPoint(3F, -1.9F, -0.5F);
		index2.setTextureSize(64, 32);
		index2.mirror = true;
		setRotation(index2, 0.4363323F, 0F, 0F);
		index3 = new ModelRenderer(this, 0, 0);
		index3.addBox(0F, -1F, 0F, 1, 1, 1);
		index3.setRotationPoint(3F, -3.6F, -1.25F);
		index3.setTextureSize(64, 32);
		index3.mirror = true;
		setRotation(index3, 0.6108652F, 0F, 0F);
		middle1 = new ModelRenderer(this, 0, 0);
		middle1.addBox(0F, -2F, 0F, 1, 2, 1);
		middle1.setRotationPoint(1.7F, 0F, 0F);
		middle1.setTextureSize(64, 32);
		middle1.mirror = true;
		setRotation(middle1, 0.2617994F, 0F, 0F);
		middle2 = new ModelRenderer(this, 0, 0);
		middle2.addBox(0F, -2F, 0F, 1, 2, 1);
		middle2.setRotationPoint(1.7F, -1.9F, -0.5F);
		middle2.setTextureSize(64, 32);
		middle2.mirror = true;
		setRotation(middle2, 0.4363323F, 0F, 0F);
		middle3 = new ModelRenderer(this, 0, 0);
		middle3.addBox(0F, -1F, 0F, 1, 1, 1);
		middle3.setRotationPoint(1.7F, -3.7F, -1.3F);
		middle3.setTextureSize(64, 32);
		middle3.mirror = true;
		setRotation(middle3, 0.6108652F, 0F, 0F);
		ring1 = new ModelRenderer(this, 0, 0);
		ring1.addBox(0F, -2F, 0F, 1, 2, 1);
		ring1.setRotationPoint(0.4F, 0F, 0F);
		ring1.setTextureSize(64, 32);
		ring1.mirror = true;
		setRotation(ring1, 0.2617994F, 0F, 0F);
		ring2 = new ModelRenderer(this, 0, 0);
		ring2.addBox(0F, -2F, 0F, 1, 2, 1);
		ring2.setRotationPoint(0.4F, -1.9F, -0.5F);
		ring2.setTextureSize(64, 32);
		ring2.mirror = true;
		setRotation(ring2, 0.4363323F, 0F, 0F);
		ring3 = new ModelRenderer(this, 0, 0);
		ring3.addBox(0F, -1F, 0F, 1, 1, 1);
		ring3.setRotationPoint(0.4F, -3.5F, -1.2F);
		ring3.setTextureSize(64, 32);
		ring3.mirror = true;
		setRotation(ring3, 0.6108652F, 0F, 0F);
		little1 = new ModelRenderer(this, 0, 0);
		little1.addBox(0F, -2F, 0F, 1, 2, 1);
		little1.setRotationPoint(-1F, 0F, 0F);
		little1.setTextureSize(64, 32);
		little1.mirror = true;
		setRotation(little1, 0.2617994F, 0F, 0F);
		little2 = new ModelRenderer(this, 0, 0);
		little2.addBox(0F, -1F, 0F, 1, 1, 1);
		little2.setRotationPoint(-1F, -1.9F, -0.5F);
		little2.setTextureSize(64, 32);
		little2.mirror = true;
		setRotation(little2, 0.4363323F, 0F, 0F);
		little3 = new ModelRenderer(this, 0, 0);
		little3.addBox(0F, -1F, 0F, 1, 1, 1);
		little3.setRotationPoint(-1F, -2.8F, -0.9F);
		little3.setTextureSize(64, 32);
		little3.mirror = true;
		setRotation(little3, 0.6108652F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		palm1.render(f5);
		palm2.render(f5);
		palm3.render(f5);
		thumb1.render(f5);
		thumb2.render(f5);
		index1.render(f5);
		index2.render(f5);
		index3.render(f5);
		middle1.render(f5);
		middle2.render(f5);
		middle3.render(f5);
		ring1.render(f5);
		ring2.render(f5);
		ring3.render(f5);
		little1.render(f5);
		little2.render(f5);
		little3.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

}
