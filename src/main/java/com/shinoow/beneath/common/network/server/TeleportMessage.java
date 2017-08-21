package com.shinoow.beneath.common.network.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;
import com.shinoow.beneath.common.network.AbstractMessage.AbstractServerMessage;
import com.shinoow.beneath.common.world.TeleporterDeepDank;

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

	    //pos is the location of the teleporter block
		BlockPos pos = new BlockPos(x, y, z);

		if(player instanceof EntityPlayerMP){
			EntityPlayerMP thePlayer = (EntityPlayerMP)player;

			if (thePlayer.timeUntilPortal > 0)
				thePlayer.timeUntilPortal = thePlayer.getPortalCooldown();
			else {
                //always move the player on to the top of the teleporter
                thePlayer.setPositionAndUpdate(pos.getX(), pos.getY() + 1, pos.getZ());
			    if (thePlayer.dimension != Beneath.dim)
    			{
    				thePlayer.timeUntilPortal = 10;
    				thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, Beneath.dim, new TeleporterDeepDank(thePlayer.mcServer.getWorld(Beneath.dim), pos, thePlayer.world.provider));
    			}
    			else {
    				thePlayer.timeUntilPortal = 10;
    				if(Beneath.dimTeleportation){
    					TileEntityTeleporterDeepDank tile = (TileEntityTeleporterDeepDank)thePlayer.world.getTileEntity(pos);
    					thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, tile.getDimension(), new TeleporterDeepDank(thePlayer.mcServer.getWorld(tile.getDimension()), pos, thePlayer.world.provider));
    				} else thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, 0, new TeleporterDeepDank(thePlayer.mcServer.getWorld(0), pos, thePlayer.world.provider));
    			}
			}
		}
	}
}
