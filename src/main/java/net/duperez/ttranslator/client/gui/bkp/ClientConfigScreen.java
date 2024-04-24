package net.duperez.ttranslator.client.gui.bkp;

import net.duperez.ttranslator.client.entities.ClientTranslationUiConfigEntity;
import net.duperez.ttranslator.client.services.ClientSideUiConfigsService;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.duperez.ttranslator.client.gui.UiCommons.BUTTON_SELECTED_COLOR;
import static net.duperez.ttranslator.client.gui.UiCommons.BUTTON_DEFAULT_COLOR;

public class ClientConfigScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int HEIGHT_INCREMENT = 24;



    public ClientConfigScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        int displayWidth = this.width / 2 - 100;
        int displayHeight = this.height / 4 - 10;

        ClientTranslationUiConfigEntity clientConfig = ClientSideUiConfigsService.getInstance().getConfigs();

        addButton(displayWidth, displayHeight, "Show translations", clientConfig::setShowTranslations, clientConfig::isShowTranslations);
        addButton(displayWidth, displayHeight + HEIGHT_INCREMENT * 2, "Show original messages", clientConfig::setShowOriginalMessage, clientConfig::isShowOriginalMessage);
        addButton(displayWidth, displayHeight + HEIGHT_INCREMENT * 3, "show your own messages translated", clientConfig::setShowOwnTranslations, clientConfig::isShowOwnTranslations);
        addButton(displayWidth, displayHeight + HEIGHT_INCREMENT * 4, "show original language", clientConfig::setShowOriginalLanguage, clientConfig::isShowOriginalLanguage);
        addButton(displayWidth, displayHeight + HEIGHT_INCREMENT * 5, "show translated language", clientConfig::setShowTranslatedLanguage, clientConfig::isShowTranslatedLanguage);
    }

    private void addButton(int displayWidth, int displayHeight, String message, Consumer<Boolean> setConfigValue, Supplier<Boolean> getConfigValue) {
        Button button = new Button(displayWidth, displayHeight, BUTTON_WIDTH, BUTTON_HEIGHT, new TextComponent(message), btn -> updateButton(setConfigValue, getConfigValue, btn));
        button.setFGColor(getConfigValue.get() ? BUTTON_SELECTED_COLOR : BUTTON_DEFAULT_COLOR);
        this.addRenderableWidget(button);
    }

    private void updateButton(Consumer<Boolean> setConfigValue, Supplier<Boolean> getConfigValue, Button btn) {
        setConfigValue.accept(!getConfigValue.get());
        btn.setFGColor(getConfigValue.get() ? BUTTON_SELECTED_COLOR : BUTTON_DEFAULT_COLOR);
    }
}
