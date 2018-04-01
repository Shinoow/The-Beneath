package com.shinoow.beneath.common.network.server;

import java.io.IOException;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;
import com.shinoow.beneath.common.network.AbstractMessage.AbstractServerMessage;
import com.shinoow.beneath.common.world.TeleporterDeepDank;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

public class TeleportMessage extends AbstractServerMessage<TeleportMessage> {

	private int x, y, z;

	public TeleportMessage() {}

	public TeleportMessage(BlockPos pos){
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException {

		x = ByteBufUtils.readVarInt(buffer, 5);
		y = ByteBufUtils.readVarInt(buffer, 5);
		z = ByteBufUtils.readVarInt(buffer, 5);
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {

		ByteBufUtils.writeVarInt(buffer, x, 5);
		ByteBufUtils.writeVarInt(buffer, y, 5);
		ByteBufUtils.writeVarInt(buffer, z, 5);
	}

	@Override
	public void process(EntityPlayer player, Side side) {

		BlockPos pos = new BlockPos(x, y, z);

		if(player instanceof EntityPlayerMP){
			EntityPlayerMP thePlayer = (EntityPlayerMP)player;

			if(!thePlayer.isRiding() && !thePlayer.isBeingRidden()) {
				if (thePlayer.timeUntilPortal > 0)
					thePlayer.timeUntilPortal = thePlayer.getPortalCooldown();
				else if (thePlayer.dimension != Beneath.dim)
				{
					thePlayer.timeUntilPortal = 10;
					if(thePlayer.dimension == 1 && Beneath.dimTeleportation)
						thePlayer.setPositionAndUpdate(pos.getX(), pos.getY() + 1, pos.getZ());
					thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, Beneath.dim, new TeleporterDeepDank(thePlayer.mcServer.worldServerForDimension(Beneath.dim), pos, thePlayer.world.provider));
				}
				else {
					thePlayer.timeUntilPortal = 10;
					if(Beneath.dimTeleportation){
						TileEntityTeleporterDeepDank tile = (TileEntityTeleporterDeepDank)thePlayer.world.getTileEntity(pos);
						thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, tile.getDimension(), new TeleporterDeepDank(thePlayer.mcServer.worldServerForDimension(tile.getDimension()), pos, thePlayer.world.provider));
					} else thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, 0, new TeleporterDeepDank(thePlayer.mcServer.worldServerForDimension(0), pos, thePlayer.world.provider));
				}
			} else {
				Entity mount = thePlayer.getRidingEntity();
				if(mount != null) {
					thePlayer.dismountRidingEntity();
					if(thePlayer.getRNG().nextInt(10) == 0) {
						mount.setDead();
						thePlayer.sendMessage(new TextComponentString("Your mount crashed into a diamond car made out of 400 walls/hour at the speed of iron cars/km"));
						thePlayer.sendMessage(new TextComponentString("It's in a better place now (far away from you)"));
					} else thePlayer.sendStatusMessage(new TextComponentString("You can't enter The Beneath while riding something!"), true);
				}
				if(thePlayer.isBeingRidden()) {
					thePlayer.removePassengers();
					thePlayer.sendStatusMessage(new TextComponentString("You can't enter The Beneath while something is riding on your back!"), true);
				}
			}
		}
	}
}
