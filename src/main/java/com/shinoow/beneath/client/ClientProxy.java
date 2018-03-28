package com.shinoow.beneath.client;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.client.handler.BeneathClientEventHandler;
import com.shinoow.beneath.client.render.entity.RenderHand;
import com.shinoow.beneath.client.render.entity.RenderShadow;
import com.shinoow.beneath.common.CommonProxy;
import com.shinoow.beneath.common.entity.EntityHand;
import com.shinoow.beneath.common.entity.EntityShadow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(){
		RenderingRegistry.registerEntityRenderingHandler(EntityShadow.class, manager -> new RenderShadow(manager));
		RenderingRegistry.registerEntityRenderingHandler(EntityHand.class, manager -> new RenderHand(manager));

		MinecraftForge.EVENT_BUS.register(new BeneathClientEventHandler());
	}

	@Override
	public void init(){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(Beneath.teleporter), 0, new ModelResourceLocation("beneath:teleporterbeneath", "inventory"));
	}

	@Override
	public void postInit(){}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		// Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
		// your packets will not work because you will be getting a client
		// player even when you are on the server! Sounds absurd, but it's true.

		// Solution is to double-check side before returning the player:
		return ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx);
	}

	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx);
	}
}
