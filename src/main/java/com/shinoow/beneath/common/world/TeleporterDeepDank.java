package com.shinoow.beneath.common.world;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;


public class TeleporterDeepDank extends Teleporter {

	private WorldServer worldServer;
	private BlockPos pos;
	private int prevDim;
	private double movementFactor;

	public TeleporterDeepDank(WorldServer par1WorldServer, BlockPos pos, WorldProvider provider){
		super(par1WorldServer);
		worldServer = par1WorldServer;
		prevDim = provider.getDimension();
		movementFactor = provider.getMovementFactor() / worldServer.provider.getMovementFactor();
		this.pos = Beneath.dimTeleportation ? new BlockPos(pos.getX() * movementFactor, pos.getY(), pos.getZ() * movementFactor) : pos;
	}

	@Override
	public void placeInPortal(Entity entity, float rotationYaw){
	    
        if (!this.moveToExistingPortal(entity, rotationYaw))
        {
            this.moveToNewPortal(entity);
        }
        
        if(Beneath.dimTeleportation){
            if(prevDim != Beneath.dim){
                TileEntity te = worldServer.getTileEntity(pos);
                if(te != null && te instanceof TileEntityTeleporterDeepDank)
                    ((TileEntityTeleporterDeepDank)te).setDimension(prevDim);
            }
            if(movementFactor > 1){
                double x = pos.getX() > entity.posX ? pos.getX() - 0.5 : pos.getX() + 1.5;
                double z = pos.getZ() > entity.posZ ? pos.getZ() - 0.5 : pos.getZ() + 1.5;
                entity.setPositionAndUpdate(MathHelper.floor(x), MathHelper.floor(entity.posY), MathHelper.floor(z));
            }
            if(entity.dimension == 1) entity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        }
        entity.setPosition(MathHelper.floor(entity.posX) + 0.5D, entity.posY + 1.0, MathHelper.floor(entity.posZ) + 0.5D);
        entity.motionX = entity.motionY = entity.motionZ = 0;
        if(entity.dimension == Beneath.dim)
            worldServer.playSound(null, entity.getPosition(), getSound(entity, worldServer.rand), SoundCategory.HOSTILE, 1, 1);

    }
	
	private boolean moveToExistingPortal(Entity entity, float rotationYaw)
	{
	    int searchRange = Beneath.teleporterLinkRange;
        BlockPos playerPos = new BlockPos(entity);
        double nearestDistance = -1.0D;
        BlockPos nearestPortal = BlockPos.ORIGIN;
        BlockPos thisBlock;
        int maxY = this.worldServer.getActualHeight() - 1;
        int minY = 0;
        
        if(Beneath.strictLinkRangeCheck) {
            maxY = Math.min(maxY, (int)entity.posY + searchRange);
            minY = Math.max(minY, (int)entity.posY - searchRange);
        }

        for (int i = (int)entity.posX - searchRange; i <= (int)entity.posX + searchRange; ++i)
        {
            for (int k = (int)entity.posZ - searchRange; k <= (int)entity.posZ + searchRange; ++k)
            {
                for (int j = maxY; j >= minY; --j)
                {
                    thisBlock = new BlockPos(i, j, k);

                    if (this.worldServer.getBlockState(thisBlock).getBlock() == Beneath.teleporter
                            && this.worldServer.isAirBlock(thisBlock.up())
                            && this.worldServer.isAirBlock(thisBlock.up(2))
                            )
                    {
                        double thisDistance = thisBlock.distanceSq(playerPos);
                        if (nearestDistance < 0.0D || thisDistance < nearestDistance)
                        {
                            if (!Beneath.strictLinkRangeCheck || thisDistance < searchRange * searchRange) {
                                nearestDistance = thisDistance;
                                nearestPortal = thisBlock;                                
                            }
                        }
                    }
                }
            }
        }
        
        if (nearestDistance > -1.0D) {
            //found one!
            entity.setPosition(nearestPortal.getX(), nearestPortal.getY() + 1.0D, nearestPortal.getZ());
            entity.motionX = entity.motionY = entity.motionZ = 0.0D;

            return true;
        }
        else {
            return false;
        }

	}
	
	private void moveToNewPortal(Entity entity) {
        //doesn't matter what dimension we are going to, we need a surface to spawn on
	    
	    //if our arriving location is in mid-air
		if(worldServer.isAirBlock(pos) && worldServer.isAirBlock(pos.up())) {
		    pos = pos.down();
		    //search for the next solid block below us
		    while(worldServer.isAirBlock(pos) && pos.getY() > 0) {
                pos = pos.down();			        
		    }
		    if (pos.getY() < 1) {
		        //if (this is a void world) then just make the portal where we stand
		        makePortalSpace(entity.getPosition());
		        pos = entity.getPosition();
		    }	
		    else
		        //move the player to the location we found
		        entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
		}
		else
		    //if the arrival location is underground
		    makePortalSpace(pos);

		//only place a new teleporter block if we are arriving into The Beneath
		if (entity.dimension == Beneath.dim) {
            worldServer.setBlockState(pos.down(), Beneath.teleporter.getDefaultState());
        }

	}
	
    private void makePortalSpace(BlockPos centeredOn) {
        for(int x = -2; x < 3; x++)
        for(int y = -1; y < 3; y++)
            for(int z = -2; z < 3; z++)
                if(y > -1)
                    worldServer.setBlockToAir(centeredOn.add(x, y, z));
                else if(worldServer.isAirBlock(centeredOn.add(x, y, z)))
                    worldServer.setBlockState(centeredOn.add(x, y, z), Blocks.STONE.getDefaultState());
    }

	private SoundEvent getSound(Entity entity, Random rand){
		SoundEvent sound = Beneath.beneath_normal;
		boolean dark = entity.getName().equalsIgnoreCase("dark"+"osto") && rand.nextInt(4) == 0;

		switch(rand.nextInt(4)){
		case 0:
			sound = Beneath.beneath_normal;
			break;
		case 1:
			sound = Beneath.beneath_muffled;
			break;
		case 2:
			sound = Beneath.beneath_drawnout;
			break;
		case 3:
			sound = Beneath.deepdank;
			break;
		}
		return dark ? rand.nextBoolean() ? Beneath.dark2 : Beneath.dark1 : sound;
	}
}