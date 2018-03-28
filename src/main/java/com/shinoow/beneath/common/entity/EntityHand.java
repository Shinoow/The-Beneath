package com.shinoow.beneath.common.entity;


import java.util.List;

import com.shinoow.beneath.common.network.PacketDispatcher;
import com.shinoow.beneath.common.network.client.SetTimerMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class EntityHand extends Entity {

	private static final DataParameter<Integer> DW_SHADOW_ID = EntityDataManager.<Integer>createKey(EntityFishHook.class, DataSerializers.VARINT);

	public EntityShadow shadow = null;

	public EntityHand(World world) {
		super(world);
	}

	public EntityHand(World world, EntityShadow entity, Entity target) {
		super(world);
		shadow = entity;
		entity.setHand(this);
		setLocationAndAngles(entity.posX, entity.posY + 1.62, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		posX -= MathHelper.cos(rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		posY -= 0.1;
		posZ -= MathHelper.sin(rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = (target.posX - entity.posX) * 0.7;
		motionY = (target.posY + target.getEyeHeight() - 0.7 - posY) * 0.7;
		motionZ = (target.posZ - entity.posZ) * 0.7;
		double vH = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
		if (vH >= 1E-7) {
			rotationYaw = (float)(Math.atan2(motionZ, motionX) * 180.0 / Math.PI) - 90.0F;
			rotationPitch = (float)(-Math.atan2(motionY, vH) * 180.0 / Math.PI);
			double dX = motionX / vH;
			double dZ = motionZ / vH;
			setLocationAndAngles(entity.posX + dX, posY, entity.posZ + dZ, rotationYaw, rotationPitch);
			//            this.yOffset = 0.0F;
			calculateVelocity(motionX, motionY + vH * 0.2, motionZ, 1.0F, 14 - (world.getDifficulty().getDifficultyId() << 2));
		}
		updateAnglerId();
	}

	@Override
	protected void entityInit() {
		setSize(0.25F, 0.25F);
		getDataManager().register(EntityHand.DW_SHADOW_ID, Integer.valueOf(0));
	}


	public int getShadowId() {
		return getDataManager().get(EntityHand.DW_SHADOW_ID);
	}

	public void updateAnglerId() {
		if (shadow != null && shadow.getEntityId() != getShadowId())
			getDataManager().set(EntityHand.DW_SHADOW_ID, Integer.valueOf(shadow.getEntityId()));
	}

	@Override
	public boolean isInRangeToRenderDist(double d) {
		double d1 = getEntityBoundingBox().getAverageEdgeLength() * 256.0;
		return d < d1 * d1;
	}

	public void calculateVelocity(double vX, double vY, double vZ, float v, float variance) {
		float vi = MathHelper.sqrt(vX * vX + vY * vY + vZ * vZ);
		vX /= vi;
		vY /= vi;
		vZ /= vi;
		vX += rand.nextGaussian() * 0.0075 * variance;
		vY += rand.nextGaussian() * 0.0075 * variance;
		vZ += rand.nextGaussian() * 0.0075 * variance;
		vX *= v;
		vY *= v;
		vZ *= v;
		motionX = vX;
		motionY = vY;
		motionZ = vZ;
		float vH = MathHelper.sqrt(vX * vX + vZ * vZ);
		prevRotationYaw = rotationYaw = (float)(Math.atan2(vX, vZ) * 180.0 / Math.PI);
		prevRotationPitch = rotationPitch = (float)(Math.atan2(vY, vH) * 180.0 / Math.PI);
	}

	@Override
	public void setVelocity(double vX, double vY, double vZ) {
		motionX = vX;
		motionY = vY;
		motionZ = vZ;
		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			float vH = MathHelper.sqrt(vX * vX + vZ * vZ);
			prevRotationYaw = rotationYaw = (float)(Math.atan2(vX, vZ) * 180.0 / Math.PI);
			prevRotationPitch = rotationPitch = (float)(Math.atan2(vY, vH) * 180.0 / Math.PI);
		}
	}

	@Override
	public void onUpdate() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		super.onUpdate();
		if (!world.isRemote) {
			if (shadow == null || shadow.isDead || getDistanceSqToEntity(shadow) > 1024.0)
				setDead();
			Vec3d posVec = new Vec3d(posX, posY, posZ);
			Vec3d motionVec = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
			RayTraceResult object = world.rayTraceBlocks(posVec, motionVec);
			posVec = new Vec3d(posX, posY, posZ);
			motionVec = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
			if (object != null)
				motionVec = new Vec3d(object.hitVec.x, object.hitVec.y, object.hitVec.z);
			Entity entityHit = null;
			List entitiesInPath = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(motionX, motionY, motionZ).grow(1.0, 1.0, 1.0));
			double d = Double.POSITIVE_INFINITY;
			for (int i = 0; i < entitiesInPath.size(); i++) {
				Entity entityInPath = (Entity) entitiesInPath.get(i);
				if (entityInPath.canBeCollidedWith() && !entityInPath.isEntityEqual(shadow)) {
					AxisAlignedBB aabb = entityInPath.getEntityBoundingBox().expand(0.3, 0.3, 0.3);
					RayTraceResult object1 = aabb.calculateIntercept(posVec, motionVec);
					if (object1 != null) {
						double d1 = posVec.distanceTo(object1.hitVec);
						if (d1 < d) {
							entityHit = entityInPath;
							d = d1;
						}
					}
				}
			}
			if (entityHit != null)
				object = new RayTraceResult(entityHit);
			if (object != null)
				onImpact(object);
		}
		else if (shadow == null) {
			Entity entity = world.getEntityByID(getShadowId());
			if (entity instanceof EntityShadow)
				shadow = (EntityShadow) entity;
		}
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float var16 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0 / Math.PI);
		for (rotationPitch = (float)(Math.atan2(motionY, var16) * 180.0 / Math.PI); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {
			// Do nothing
		}
		while (rotationPitch - prevRotationPitch >= 180.0F)
			prevRotationPitch += 360.0F;
		while (rotationYaw - prevRotationYaw < -180.0F)
			prevRotationYaw -= 360.0F;
		while (rotationYaw - prevRotationYaw >= 180.0F)
			prevRotationYaw += 360.0F;
		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		if (isInWater())
			setDead();
		motionX *= 0.99;
		motionY *= 0.99;
		motionZ *= 0.99;
		motionY -= getGravityVelocity();
		setPosition(posX, posY, posZ);
	}

	public void onImpact(RayTraceResult object) {
		if (object.entityHit != null) {
			double vX = shadow.posX - posX;
			double vY = shadow.posY - posY;
			double vZ = shadow.posZ - posZ;
			double v = Math.sqrt(vX * vX + vY * vY + vZ * vZ);
			double mult = 0.31;
			object.entityHit.motionX = vX * mult;
			object.entityHit.motionY = vY * mult + Math.sqrt(v) * 0.1;
			object.entityHit.motionZ = vZ * mult;
			object.entityHit.onGround = false;
			if (object.entityHit instanceof EntityPlayerMP) {
				PacketDispatcher.sendTo(new SetTimerMessage(), (EntityPlayerMP) object.entityHit);
				try {
					((EntityPlayerMP) object.entityHit).connection.sendPacket(new SPacketEntityVelocity(object.entityHit));
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		setDead();
	}

	protected float getGravityVelocity() {
		return 0.03F;
	}

	@Override
	public void setDead() {
		if (shadow != null)
			shadow.setHand(null);
		super.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {}
	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {}
}