package com.shinoow.beneath.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {

	public void preInit(){}

	public void init(){}

	public void postInit(){}

	/**
	 * Returns a side-appropriate EntityPlayer for use during message handling
	 */
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

	/**
	 * Returns the current thread based on side during message handling,
	 * used for ensuring that the message is being handled by the main thread
	 */
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return ctx.getServerHandler().player.getServer();
	}
}
