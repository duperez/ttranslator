package net.duperez.ttranslator.client.gui.languageConfig;

import com.google.gson.Gson;
import net.duperez.ttranslator.client.entities.ClientSideClientConfigs;
import net.duperez.ttranslator.client.gui.baseScreens.BaseTTranslatorPaginatedScreen;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.client.services.ClientSideClientModService;
import net.duperez.ttranslator.common.messages.UserPackage;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.objects.common.Language;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReadLanguageScreen extends BaseTTranslatorPaginatedScreen {

    List<String> readLanguageIsoNames;

    private static final int BUTTON_WIDTH = 100;
    private static final int HALF_BUTTON_WIDTH = BUTTON_WIDTH / 2;

    protected ReadLanguageScreen(Component pTitle) {
        super(pTitle);
        this.readLanguageIsoNames = getReadLanguageIsoNames();
    }

    private List<String> getReadLanguageIsoNames() {
        return ClientSideClientLanguageService.getInstance().getClientEntity().getReadLanguage().stream()
                .map(Language::getIsoName)
                .collect(Collectors.toList());
    }

    @Override
    public void init() {
        super.init();
        renderMenu();
    }

    protected void renderMenu() {
        this.clearWidgets();
        int height = this.height / MAX_ITEMS_PER_PAGE - 10;

        int startIndex = (currentPage - 1) * MAX_ITEMS_PER_PAGE;

        super.addDefaultBackButton(new MainLanguageConfigScreen(new TextComponent("Main language menu")));


        List<Language> languages = new ArrayList<>(ClientSideClientModService.getInstance().getClientEntity().getLanguages());

        totalPages = getTotalPages();

        int endIndex = Math.min(startIndex + 4, languages.size());


        for (int i = startIndex; i < endIndex; i++) {
            Language language = languages.get(i);
            addButton(language, height + 20, 20, button -> selectLanguage(button, language));
            height += 25;
        }

        addDefaultPaginationController();
    }

    private void selectLanguage(Button button, Language language) {
        ClientSideClientConfigs userConfigs = ClientSideClientLanguageService.getInstance().getClientEntity();
        ClientSideClientLanguageService.getInstance().getClientEntity().getReadLanguage().stream().filter(l -> l.getIsoName().equals(language.getIsoName())).findFirst().ifPresentOrElse(l -> {
            ClientSideClientLanguageService.getInstance().getClientEntity().getReadLanguage().remove(language);
            button.setFGColor(BUTTON_DEFAULT_COLOR);
        }, () -> {
            ClientSideClientLanguageService.getInstance().getClientEntity().getReadLanguage().add(language);
            button.setFGColor(BUTTON_SELECTED_COLOR);
        });
        String userConfigsJson = new Gson().toJson(userConfigs);
        UserPackage userPackage = new UserPackage(userConfigsJson);
        getModNetworkingChannel().sendToServer(userPackage);
    }

    private SimpleChannel getModNetworkingChannel() {
        return ModNetworking.CHANNEL;
    }

    private void addButton(Language language, int y, int height, Button.OnPress onPress) {
        String label = language.getName();
        int x = this.width / 2 - HALF_BUTTON_WIDTH; // Center of screen adjusted to button width

        Button button = new Button(x, y, BUTTON_WIDTH, height, new TextComponent(label), onPress);
        if (readLanguageIsoNames.contains(language.getIsoName())) {
            button.setFGColor(BUTTON_SELECTED_COLOR);
        } else {
            button.setFGColor(BUTTON_DEFAULT_COLOR);
        }
        this.addRenderableWidget(button);
    }
}
