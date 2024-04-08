package net.duperez.ttranslator.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.duperez.ttranslator.entities.ClientConfig;
import net.duperez.ttranslator.services.client.ClientUserService;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ClientConfigScreen extends Screen {
    protected ClientConfigScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        int width = this.width / 2 - 100; // Centraliza os botões na tela
        int height = this.height / 4 - 10;

        int plusHeight = 0;

        ClientConfig clientCOnfig = ClientUserService.getInstance().getClientConfig();

        Button showTranslationButton = new Button(width, height + plusHeight, 200, 20, new TextComponent("Show translations"), button -> {
            clientCOnfig.setShowTranslations(!clientCOnfig.isShowTranslations());
            button.setFGColor(clientCOnfig.isShowTranslations() ? 0xFFFF00 : 0xFFFFFF);
        });
        showTranslationButton.setFGColor(clientCOnfig.isShowTranslations() ? 0xFFFF00 : 0xFFFFFF);

        // Botão para "Spoken Language"
        this.addRenderableWidget(showTranslationButton);
        plusHeight+=24;

        Button showOwnTranslationButton = new Button(width, height + plusHeight, 200, 20, new TextComponent("show your own messages translated"), button -> {
            clientCOnfig.setShowOwnTranslations(!clientCOnfig.isShowOwnTranslations());
            button.setFGColor(clientCOnfig.isShowOwnTranslations() ? 0xFFFF00 : 0xFFFFFF);
        });
        showOwnTranslationButton.setFGColor(clientCOnfig.isShowOwnTranslations() ? 0xFFFF00 : 0xFFFFFF);

        this.addRenderableWidget(showOwnTranslationButton);
        plusHeight+=24;

        Button showOriginalLanguageButton = new Button(width, height + plusHeight, 200, 20, new TextComponent("show original language"), button -> {
            clientCOnfig.setShowOriginalLanguage(!clientCOnfig.isShowOriginalLanguage());
            button.setFGColor(clientCOnfig.isShowOriginalLanguage() ? 0xFFFF00 : 0xFFFFFF);
        });
        showOriginalLanguageButton.setFGColor(clientCOnfig.isShowOriginalLanguage() ? 0xFFFF00 : 0xFFFFFF);

        this.addRenderableWidget(showOriginalLanguageButton);
        plusHeight+=24;

        Button showTranslatedLanguageButton = new Button(width, height + plusHeight, 200, 20, new TextComponent("show translated language"), button -> {
            clientCOnfig.setShowTranslatedLanguage(!clientCOnfig.isShowTranslatedLanguage());
            button.setFGColor(clientCOnfig.isShowTranslatedLanguage() ? 0xFFFF00 : 0xFFFFFF);
        });
        showTranslatedLanguageButton.setFGColor(clientCOnfig.isShowTranslatedLanguage() ? 0xFFFF00 : 0xFFFFFF);

        this.addRenderableWidget(showTranslatedLanguageButton);
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
