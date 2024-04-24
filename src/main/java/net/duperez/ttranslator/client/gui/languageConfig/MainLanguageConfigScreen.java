package net.duperez.ttranslator.client.gui.languageConfig;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class MainLanguageConfigScreen extends Screen {
    public MainLanguageConfigScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        int width = this.width / 2 - 100; // Centraliza os botões na tela
        int height = this.height / 4 - 10;
        int heightAdd = 24;

        this.addRenderableWidget(new Button(width, height, 200, 20, new TextComponent("Read Language configs"), button -> this.minecraft.setScreen(new ReadLanguageScreen(new TextComponent("Read Language configs")))));
        heightAdd += heightAdd;
        this.addRenderableWidget(new Button(width, height + heightAdd, 200, 20, new TextComponent("Spoken language configs"), button -> this.minecraft.setScreen(new SpokenLanguageScreen(new TextComponent("Spoken language configs")))));


        //SpokenLanguageScreen
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


}
