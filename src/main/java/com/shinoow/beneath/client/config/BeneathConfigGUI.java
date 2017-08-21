package com.shinoow.beneath.client.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

import com.shinoow.beneath.Beneath;

public class BeneathConfigGUI extends GuiConfig {

	public BeneathConfigGUI(GuiScreen parent) {
		super(parent, new ConfigElement(Beneath.cfg.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "beneath", true, true, "The Beneath");
	}
}