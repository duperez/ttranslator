package net.duperez.ttranslator.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.duperez.ttranslator.messages.TranslationKeyPackage;
import net.duperez.ttranslator.network.ModNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ServerConfigScreen extends Screen {

    protected ServerConfigScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        int width = this.width / 2 - 100; // Centraliza os botões na tela
        int height = this.height / 4 - 10;

        // Adicionando o EditBox
        EditBox textField = new EditBox(this.font, width, height, 200, 20, new TextComponent("Text Field"));
        textField.setMaxLength(40); // Define o máximo de caracteres permitidos
        textField.setValue(""); // Valor inicial do campo de texto
        addRenderableWidget(textField); // Adiciona o campo de texto para que ele seja renderizado e atualizado


        // Botão para "Spoken Language"
        this.addRenderableWidget(new Button(width, height + 30, 200, 20, new TextComponent("save translation key"), button -> {
            TranslationKeyPackage translationKeyPackage = new TranslationKeyPackage(textField.getValue());
            ModNetworking.CHANNEL.sendToServer(translationKeyPackage);
        }));

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
