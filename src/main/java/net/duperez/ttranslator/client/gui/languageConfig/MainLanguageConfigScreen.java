package net.duperez.ttranslator.client.gui.languageConfig;

import net.duperez.ttranslator.client.gui.MainScreen;
import net.duperez.ttranslator.client.gui.baseScreens.BaseTTranslatorScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class MainLanguageConfigScreen extends BaseTTranslatorScreen {
    public MainLanguageConfigScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        int width = this.width / 2 - 100; // Centraliza os botões na tela
        int height = this.height / 4 - 10;
        int heightAdd = 24;

        super.addDefaultBackButton(new MainScreen(new TextComponent("Main menu")));

        this.addRenderableWidget(new Button(width, height, 200, 20, new TextComponent("Read Language configs"), button -> this.minecraft.setScreen(new ReadLanguageScreen(new TextComponent("Read Language configs")))));
        heightAdd += heightAdd;
        this.addRenderableWidget(new Button(width, height + heightAdd, 200, 20, new TextComponent("Spoken language configs"), button -> this.minecraft.setScreen(new SpokenLanguageScreen(new TextComponent("Spoken language configs")))));
    }
}
