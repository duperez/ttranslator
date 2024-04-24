package net.duperez.ttranslator.client.gui.languageConfig;

import com.google.gson.Gson;
import com.mojang.blaze3d.vertex.PoseStack;
import net.duperez.ttranslator.client.entities.ClientSideClientConfigs;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.client.services.ClientSideClientModService;
import net.duperez.ttranslator.common.messages.UserPackage;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.objects.common.Language;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.duperez.ttranslator.client.gui.UiCommons.BUTTON_DEFAULT_COLOR;
import static net.duperez.ttranslator.client.gui.UiCommons.BUTTON_SELECTED_COLOR;

public class ReadLanguageScreen extends Screen {

    private int currentPage = 1;
    private int totalPages;

    List<String> readLanguageIsoNames;

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
        totalPages = (int) Math.ceil((double) ClientSideClientModService.getInstance().getClientEntity().getLanguages().size() / 4);
        renderMenu();
    }


    private void renderMenu() {
        this.clearWidgets();
        int width = this.width / 2 - 100;
        int height = this.height / 4 - 10;

        int startIndex = (currentPage - 1) * 4;

        List<Language> languages = new ArrayList<>(ClientSideClientModService
                .getInstance()
                .getClientEntity()
                .getLanguages());

        totalPages = (int) Math.ceil((double) languages.size() / 4);

        int endIndex = Math.min(startIndex + 4, languages.size());


        for (int i = startIndex; i < endIndex; i++) {
            Language language = languages.get(i);

            addButton(language, width, height + 20, 100, 20, button -> selectLanguage(button, language));

            height += 25;
        }

        addController(width, height);
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
        sendUserConfigsToServer(userConfigs);
    }

    private void sendUserConfigsToServer(ClientSideClientConfigs userConfigs) {
        String userConfigsJson = new Gson().toJson(userConfigs);
        UserPackage userPackage = new UserPackage(userConfigsJson);
        getModNetworkingChannel().sendToServer(userPackage);
    }

    private SimpleChannel getModNetworkingChannel() {
        return ModNetworking.CHANNEL;
    }

    private void addButton(Language language, int x, int y, int widht, int height, Button.OnPress onPress) {
        String label = language.getName();
        Button button = new Button(x, y, widht, height, new TextComponent(label), onPress);
        if (readLanguageIsoNames.contains(language.getIsoName())) {
            button.setFGColor(BUTTON_SELECTED_COLOR);
        } else {
            button.setFGColor(BUTTON_DEFAULT_COLOR);
        }
        this.addRenderableWidget(button);
    }

    private void addController(int width, int height) {
        Button backButton = new Button(width, height + 80, 20, 20, new TextComponent("<"), (Button button) -> {
            if(currentPage > 1) {
                currentPage--;
                renderMenu();
            }
        });
        this.addRenderableWidget(backButton);

        Button currentPageButton = new Button((width) + 25, height + 80, 20, 20, new TextComponent(currentPage + "/" + totalPages), (Button button) -> {
        });
        this.addRenderableWidget(currentPageButton);

        Button forwardButton = new Button((width) + 50, height + 80, 20, 20, new TextComponent(">"), (Button button) -> {
            if(totalPages > currentPage) {
                currentPage++;
                renderMenu();
            }
        });
        this.addRenderableWidget(forwardButton);
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
