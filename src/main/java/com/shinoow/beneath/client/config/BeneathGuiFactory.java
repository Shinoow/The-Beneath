package com.shinoow.beneath.client.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class BeneathGuiFactory implements IModGuiFactory {
	
	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return BeneathConfigGUI.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@SuppressWarnings("deprecation")  //the return type is deprecated but the method must be overridden!!
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

    @Override
    public boolean hasConfigGui() {
        return false;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return null;
    }
}