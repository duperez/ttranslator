package net.duperez.ttranslator.client.gui;

import net.duperez.ttranslator.client.gui.baseScreens.BaseTTranslatorScreen;
import net.duperez.ttranslator.client.gui.bkp.ServerKeyTranslationScreen;
import net.duperez.ttranslator.client.gui.languageConfig.MainLanguageConfigScreen;
import net.duperez.ttranslator.client.gui.serverUI.ServerLanguagesScreen;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Supplier;

public class MainScreen extends BaseTTranslatorScreen {

    public MainScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        this.clearWidgets();
        super.init();
        int width = this.width / 2 - 100;
        int height = this.height / 4 - 10;

        addButton(width, height, "Language configurations",
                () -> new MainLanguageConfigScreen(new TextComponent("Language configurations")), true);
        //addButton(width, height + 24, "Client UI configuration",
        //        () -> new ClientConfigScreen(new TextComponent("Client UI configuration")));


        addButton(width, height + 24, "Server key translation Configuration",
                () -> new ServerKeyTranslationScreen(new TextComponent("Server key translation Configuration")), ClientSideClientLanguageService.getInstance().getClientEntity().isOp());
        addButton(width, height + 24 * 2, "Server languages configurations",
                () -> new ServerLanguagesScreen(new TextComponent("Server languages configurations")), ClientSideClientLanguageService.getInstance().getClientEntity().isOp());
    }

    private void addButton(int width, int height, String buttonText, Supplier<Screen> screenSupplier, boolean isAvailable) {
        Button btn = new Button(width, height, 200, 20, new TextComponent(buttonText), button -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(screenSupplier.get());
        });
        btn.active = isAvailable;
        this.addRenderableWidget(btn);
    }
}