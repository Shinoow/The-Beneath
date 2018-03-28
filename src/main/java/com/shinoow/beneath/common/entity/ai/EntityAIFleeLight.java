package com.shinoow.beneath.common.entity.ai;

import java.util.Random;

import javax.annotation.Nullable;

import com.shinoow.beneath.Beneath;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIFleeLight extends EntityAIBase
{
	private final EntityCreature theCreature;
	private double shelterX;
	private double shelterY;
	private double shelterZ;
	private final double movementSpeed;
	private final World theWorld;

	public EntityAIFleeLight(EntityCreature theCreatureIn, double movementSpeedIn)
	{
		theCreature = theCreatureIn;
		movementSpeed = movementSpeedIn;
		theWorld = theCreatureIn.world;
		setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if (theWorld.getLightBrightness(new BlockPos(theCreature.posX, theCreature.getEntityBoundingBox().minY, theCreature.posZ)) < 0.2F)
			return false;
		else
		{
			Vec3d vec3d = findPossibleShelter();

			if (vec3d == null)
				return false;
			else
			{
				shelterX = vec3d.xCoord;
				shelterY = vec3d.yCoord;
				shelterZ = vec3d.zCoord;
				return true;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		return !theCreature.getNavigator().noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		theCreature.world.playSound(null, theCreature.getPosition(), Beneath.scream, SoundCategory.HOSTILE, 1, 1);
		theCreature.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ, movementSpeed);
	}

	@Nullable
	private Vec3d findPossibleShelter()
	{
		Random random = theCreature.getRNG();
		BlockPos blockpos = new BlockPos(theCreature.posX, theCreature.getEntityBoundingBox().minY, theCreature.posZ);

		for (int i = 0; i < 10; ++i)
		{
			BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

			if (theWorld.getLightBrightness(blockpos1.up()) < 0.2)
				return new Vec3d(blockpos1.getX(), blockpos1.getY()+1, blockpos1.getZ());
		}

		return null;
	}
}