package com.shinoow.beneath.client.config;

import com.shinoow.beneath.Beneath;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class BeneathConfigGUI extends GuiConfig {

	public BeneathConfigGUI(GuiScreen parent) {
		super(parent, new ConfigElement(Beneath.cfg.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "beneath", true, true, "The Beneath");
	}
}