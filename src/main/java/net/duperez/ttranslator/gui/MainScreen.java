package net.duperez.ttranslator.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import net.minecraft.client.gui.components.Button;
import net.duperez.ttranslator.gui.guiServices.LanguageConfigScreen;

public class MainScreen extends Screen {

    public MainScreen(Component title)
    {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        int width = this.width / 2 - 100; // Centraliza os botões na tela
        int height = this.height / 4 - 10;

        // Botão para "Spoken Language"
        this.addRenderableWidget(new Button(width, height, 200, 20, new TextComponent("Spoken Language"), button -> {
            this.minecraft.setScreen(new SpokenLanguageScreen(new TextComponent("Spoken Language")));
        }));

        // Botão para "Read Language"
        this.addRenderableWidget(new Button(width, height + 24, 200, 20, new TextComponent("Read Language"), button -> {
            this.minecraft.setScreen(new ReadLanguageScreen(new TextComponent("Read Language")));
        }));

        // Botão para "Read Language"
        this.addRenderableWidget(new Button(width, height + 48, 200, 20, new TextComponent("Client configuration"), button -> {
            this.minecraft.setScreen(new ClientConfigScreen(new TextComponent("Client configuration")));
        }));

        Button serverButton = new Button(width, height + 72, 200, 20, new TextComponent("Server configuration"), button -> {
            this.minecraft.setScreen(new LanguageConfigScreen(new TextComponent("Server configuration")));
        });
        serverButton.setFGColor(0xFF0000);

        this.addRenderableWidget(serverButton);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack); // Isso vai desenhar o fundo escuro padrão
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        // Aqui você pode adicionar o que mais quiser renderizar
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Retorna true se essa tela não deve pausar o jogo no modo singleplayer
    }
}