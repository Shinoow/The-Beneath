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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;

public class TeleportMessage extends AbstractServerMessage<TeleportMessage> {

	private BlockPos pos;

	public TeleportMessage() {}

	public TeleportMessage(BlockPos pos){
		this.pos = pos;
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException {

		pos = buffer.readBlockPos();
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {

		buffer.writeBlockPos(pos);
	}

	@Override
	public void process(EntityPlayer player, Side side) {

		if(player instanceof EntityPlayerMP){
			EntityPlayerMP thePlayer = (EntityPlayerMP)player;

			if(!thePlayer.isRiding() && !thePlayer.isBeingRidden()) {
				if (thePlayer.timeUntilPortal > 0)
					thePlayer.timeUntilPortal = thePlayer.getPortalCooldown();
				else if (thePlayer.dimension != Beneath.dim)
				{
					if(!ForgeHooks.onTravelToDimension(thePlayer, Beneath.dim)) return;
					thePlayer.timeUntilPortal = 10;
					if(thePlayer.dimension == 1 && Beneath.dimTeleportation)
						thePlayer.setPositionAndUpdate(pos.getX(), pos.getY() + 1, pos.getZ());
					thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, Beneath.dim, new TeleporterDeepDank(thePlayer.mcServer.getWorld(Beneath.dim), pos, thePlayer.world.provider));
				} else if(Beneath.dimTeleportation){
					TileEntityTeleporterDeepDank tile = (TileEntityTeleporterDeepDank)thePlayer.world.getTileEntity(pos);
					if(!ForgeHooks.onTravelToDimension(thePlayer, tile.getDimension())) return;
					thePlayer.timeUntilPortal = 10;
					thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, tile.getDimension(), new TeleporterDeepDank(thePlayer.mcServer.getWorld(tile.getDimension()), pos, thePlayer.world.provider));
				} else{
					if(!ForgeHooks.onTravelToDimension(thePlayer, 0)) return;
					thePlayer.timeUntilPortal = 10;
					thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, 0, new TeleporterDeepDank(thePlayer.mcServer.getWorld(0), pos, thePlayer.world.provider));
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
