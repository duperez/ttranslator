package net.duperez.ttranslator.client.gui.languageConfig;

import com.google.gson.Gson;
import net.duperez.ttranslator.client.gui.baseScreens.BaseTTranslatorPaginatedScreen;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.client.services.ClientSideClientModService;
import net.duperez.ttranslator.common.messages.UserPackage;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.objects.common.Language;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class SpokenLanguageScreen extends BaseTTranslatorPaginatedScreen {

    private Button selectedButton;

    protected SpokenLanguageScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void init() {
        super.init();
        renderMenu();
    }


    @Override
    protected void renderMenu() {
        this.clearWidgets();
        int height = this.height / 4 - 10;

        super.addDefaultBackButton(new MainLanguageConfigScreen(new TextComponent("Main language menu")));

        int startIndex = (super.currentPage - 1) * 4;

        List<Language> languages = new ArrayList<>(ClientSideClientModService.getInstance().getClientEntity().getLanguages());

        totalPages = getTotalPages();

        int endIndex = Math.min(startIndex + 4, languages.size());


        for (int i = startIndex; i < endIndex; i++) {
            Language language = languages.get(i);
            addButton(language, height + 20, button -> selectLanguage(button, language));
            height += 25;
        }

        addDefaultPaginationController();
    }

    private void selectLanguage(Button button, Language language) {
        if (selectedButton != null) {
            selectedButton.setFGColor(BUTTON_DEFAULT_COLOR);
        }
        selectedButton = button;
        selectedButton.setFGColor(BUTTON_SELECTED_COLOR);
        ClientSideClientLanguageService.getInstance().getClientEntity().setSpokenLanguage(language);

        UserPackage userPackage = new UserPackage(new Gson().toJson(ClientSideClientLanguageService.getInstance().getClientEntity()));

        ModNetworking.CHANNEL.sendToServer(userPackage);

    }

    private void addButton(Language language, int y, Button.OnPress onPress) {
        String label = language.getName();
        int x = this.width / 2 - HALF_BUTTON_WIDTH; // Center of screen adjusted to button width
        Button button = new Button(x, y, BUTTON_WIDTH, 20, new TextComponent(label), onPress);
        if (ClientSideClientLanguageService.getInstance().getClientEntity().getSpokenLanguage() != null && ClientSideClientLanguageService.getInstance().getClientEntity().getSpokenLanguage().getIsoName().equals(language.getIsoName())) {
            button.setFGColor(BUTTON_SELECTED_COLOR);
            selectedButton = button;
        } else {
            button.setFGColor(BUTTON_DEFAULT_COLOR); //0xFFFFFF
        }
        this.addRenderableWidget(button);
    }
}
