package com.shinoow.beneath.common.world;

import java.util.Random;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;

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
	public void placeInPortal(Entity entity, float par8){
		if(worldServer.getBlockState(pos).getBlock() != Beneath.teleporter && worldServer.provider.getDimension() == Beneath.dim){
			for(int x = -2; x < 3; x++)
				for(int y = -1; y < 3; y++)
					for(int z = -2; z < 3; z++)
						if(y > -1)
							worldServer.setBlockToAir(pos.add(x, y, z));
						else if(worldServer.isAirBlock(pos.add(x, y, z)))
							worldServer.setBlockState(pos.add(x, y, z), Blocks.COBBLESTONE.getDefaultState());
			if(Beneath.teleportTorches){
				worldServer.setBlockState(pos.add(-2, 0, -2), Blocks.TORCH.getDefaultState());
				worldServer.setBlockState(pos.add(-2, 0, 2), Blocks.TORCH.getDefaultState());
				worldServer.setBlockState(pos.add(2, 0, -2), Blocks.TORCH.getDefaultState());
				worldServer.setBlockState(pos.add(2, 0, 2), Blocks.TORCH.getDefaultState());
				for(int x = -2; x < 3; x++)
					for(int y = 0; y < 3; y++)
						for(int z = -2; z < 3; z++)
							worldServer.checkLight(pos.add(x, y, z));
			}
			worldServer.setBlockState(pos, Beneath.teleporter.getDefaultState());
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
			if(worldServer.provider.getDimension() == 1) entity.setPositionAndUpdate(pos.getX(), pos.getY() + 1, pos.getZ());
		}
		entity.setPosition(MathHelper.floor(entity.posX < pos.getX() ? pos.getX() - 1 : entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ < pos.getZ() ? pos.getZ() - 1 : entity.posZ));
		entity.motionX = entity.motionY = entity.motionZ = 0;
		if(worldServer.provider.getDimension() == Beneath.dim)
			worldServer.playSound(null, entity.getPosition(), getSound(entity, worldServer.rand), SoundCategory.HOSTILE, 1, 1);
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