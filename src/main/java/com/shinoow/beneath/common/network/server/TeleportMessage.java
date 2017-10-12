package com.shinoow.beneath.common.network.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;
import com.shinoow.beneath.common.network.AbstractMessage.AbstractServerMessage;
import com.shinoow.beneath.common.world.TeleporterDeepDank;

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
		}
	}
}
