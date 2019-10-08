package com.shinoow.beneath.common.command;

import com.shinoow.beneath.common.handler.BlockDecorationHandler;
import com.shinoow.beneath.common.handler.OreGenHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandReload extends CommandBase {

	@Override
	public String getName() {

		return "beneath";
	}

	@Override
	public String getUsage(ICommandSender sender) {

		return "/beneath reloadAll | reloadOres | reloadDeco";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!sender.getEntityWorld().isRemote){
			if(args.length == 1) {
				if(args[0].equals("reloadAll")) {
					OreGenHandler.setupOregenFile();
					OreGenHandler.saveOregenFile();
					BlockDecorationHandler.setupBlockDecoFile();
					BlockDecorationHandler.saveBlockDecoFile();
				} else if(args[0].equals("reloadOres")) {
					OreGenHandler.setupOregenFile();
					OreGenHandler.saveOregenFile();
				} else if(args[0].equals("reloadDeco")) {
					BlockDecorationHandler.setupBlockDecoFile();
					BlockDecorationHandler.saveBlockDecoFile();
				}
			} else {
				sender.sendMessage(new TextComponentString(getUsage(sender)));
			}
		}
	}
}
