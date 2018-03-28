package com.shinoow.beneath.common.network.client;

import java.io.IOException;

import com.shinoow.beneath.client.handler.BeneathClientEventHandler;
import com.shinoow.beneath.common.network.AbstractMessage.AbstractClientMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class SetTimerMessage extends AbstractClientMessage<SetTimerMessage> {

	public SetTimerMessage() {}

	@Override
	protected void read(PacketBuffer buffer) throws IOException {

	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {

	}

	@Override
	public void process(EntityPlayer player, Side side) {
		BeneathClientEventHandler.startTimer();
	}

}
