package com.shinoow.beneath.client.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class BeneathGuiFactory implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return BeneathConfigGUI.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

    @Override
    public boolean hasConfigGui() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        // TODO Auto-generated method stub
        return null;
    }
}