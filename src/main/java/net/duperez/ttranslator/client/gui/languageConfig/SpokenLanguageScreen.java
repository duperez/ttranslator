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

import static net.duperez.ttranslator.client.gui.UiCommons.BUTTON_DEFAULT_COLOR;
import static net.duperez.ttranslator.client.gui.UiCommons.BUTTON_SELECTED_COLOR;

public class SpokenLanguageScreen extends Screen {

    private int currentPage = 1;
    private int totalPages;

    private Button selectedButton;

    protected SpokenLanguageScreen(Component pTitle) {
        super(pTitle);
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
        int heightSuplier = 0;

        int startIndex = (currentPage - 1) * 4;

        List<Language> languages = new ArrayList<>(ClientSideClientModService
                .getInstance()
                .getClientEntity()
                .getLanguages());

        totalPages = (int) Math.ceil((double) languages.size() / 4);

        int endIndex = Math.min(startIndex + 4, languages.size());


        for (int i = startIndex; i < endIndex; i++) {
            Language language = languages.get(i);

            addButton(language, width + 20, (height + heightSuplier) + 20, button -> selectLanguage(button, language));
            heightSuplier += 25;
        }

        addController(width, height + 100);
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

    private void sendUserConfigsToServer(ClientSideClientConfigs userConfigs) {
        String userConfigsJson = new Gson().toJson(userConfigs);
        UserPackage userPackage = new UserPackage(userConfigsJson);
        getModNetworkingChannel().sendToServer(userPackage);
    }

    private SimpleChannel getModNetworkingChannel() {
        return ModNetworking.CHANNEL;
    }

    private void addButton(Language language, int x, int y, Button.OnPress onPress) {
        String label = language.getName();
        Button button = new Button(x, y, 200, 20, new TextComponent(label), onPress);
        System.out.println();
        if (ClientSideClientLanguageService.getInstance().getClientEntity().getSpokenLanguage() != null && ClientSideClientLanguageService.getInstance().getClientEntity().getSpokenLanguage().getIsoName().equals(language.getIsoName())) {
            button.setFGColor(BUTTON_SELECTED_COLOR);
            selectedButton = button;
        } else {
            button.setFGColor(BUTTON_DEFAULT_COLOR); //0xFFFFFF
        }
        this.addRenderableWidget(button);
    }

    private void addController(int width, int height) {
        Button backButton = new Button(width + 100, height + 80, 20, 20, new TextComponent("<"), (Button button) -> {
            if(currentPage > 1) {
                currentPage--;
                renderMenu();
            }
        });
        this.addRenderableWidget(backButton);

        Button currentPageButton = new Button((width + 100) + 25, height + 80, 20, 20, new TextComponent(currentPage + "/" + totalPages), (Button button) -> {
        });
        this.addRenderableWidget(currentPageButton);

        Button forwardButton = new Button((width + 100) + 50, height + 80, 20, 20, new TextComponent(">"), (Button button) -> {
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
