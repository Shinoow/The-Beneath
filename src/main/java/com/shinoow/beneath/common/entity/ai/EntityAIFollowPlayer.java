package com.shinoow.beneath.common.entity.ai;

import java.util.List;

import com.shinoow.beneath.common.entity.EntityShadow;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIFollowPlayer extends EntityAIBase
{
	private final EntityShadow theShadow;
	private EntityPlayer thePlayer;
	public EntityAIFollowPlayer(EntityShadow theShadowIn)
	{
		theShadow = theShadowIn;
		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		List<EntityPlayer> list = theShadow.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, theShadow.getEntityBoundingBox().expand(12.0D, 2.0D, 12.0D));

		if (list.isEmpty())
			return false;
		else
		{
			for (EntityPlayer player : list)
				if (!player.capabilities.isCreativeMode)
				{
					thePlayer = player;
					break;
				}

			return thePlayer != null && theShadow.world.getLight(thePlayer.getPosition()) < 6;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting()
	{
		return theShadow.world.getLight(thePlayer.getPosition()) < 6;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		//maybe set a timer on how long it should stalk the player
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		thePlayer = null;
		theShadow.getNavigator().clearPathEntity();
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		theShadow.getLookHelper().setLookPositionWithEntity(thePlayer, 30.0F, 30.0F);

		theShadow.getNavigator().tryMoveToEntityLiving(thePlayer, 1.0D);

		if (theShadow.getDistanceSqToEntity(thePlayer) < 4.0D)
			theShadow.getNavigator().clearPathEntity();
	}
}