package net.duperez.ttranslator.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.duperez.ttranslator.client.gui.bkp.ClientConfigScreen;
import net.duperez.ttranslator.client.gui.bkp.ServerKeyTranslationScreen;
import net.duperez.ttranslator.client.gui.languageConfig.MainLanguageConfigScreen;
import net.duperez.ttranslator.client.gui.serverUI.ServerLanguagesScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import net.minecraft.client.gui.components.Button;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MainScreen extends Screen {

    public MainScreen(Component title)
    {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        int width = this.width / 2 - 100;
        int height = this.height / 4 - 10;

        addButton(width, height, "Language configurations",
                () -> new MainLanguageConfigScreen(new TextComponent("Language configurations")));
        addButton(width, height + 24, "Client UI configuration",
                () -> new ClientConfigScreen(new TextComponent("Client UI configuration")));
        addButton(width, height + 24 * 2, "Server key translation Configuration",
                () -> new ServerKeyTranslationScreen(new TextComponent("Server key translation Configuration")));
        addButton(width, height + 24 * 3, "Server languages configurations",
                () -> new ServerLanguagesScreen(new TextComponent("Server languages configurations")));
    }

    private void addButton(int width, int height, String buttonText, Supplier<Screen> screenSupplier) {
        this.addRenderableWidget(new Button(width, height, 200, 20, new TextComponent(buttonText), button -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(screenSupplier.get());
        }));
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}