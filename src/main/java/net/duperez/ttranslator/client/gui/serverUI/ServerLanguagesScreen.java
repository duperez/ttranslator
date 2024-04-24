package net.duperez.ttranslator.client.gui.serverUI;

import com.google.gson.Gson;
import com.mojang.blaze3d.vertex.PoseStack;
import net.duperez.ttranslator.client.services.ClientSideClientModService;
import net.duperez.ttranslator.common.messages.LanguagePacket;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.objects.common.Language;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class ServerLanguagesScreen extends Screen {

    private int currentPage = 1;

    List<Language> languages;

    private int totalPages;

    public ServerLanguagesScreen(Component pTitle) {
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

        int startIndex = (currentPage - 1) * 4;

        languages = new ArrayList<>(ClientSideClientModService
                .getInstance()
                .getClientEntity()
                .getLanguages());

        totalPages = (int) Math.ceil((double) languages.size() / 4);

        int endIndex = Math.min(startIndex + 4, languages.size());


        for (int i = startIndex; i < endIndex; i++) {
            Language language = languages.get(i);

            String languageName = language.getName();
            EditBox languageBox = new EditBox(this.font, width + 20, height + 20, 100, 20, new TextComponent(languageName));
            languageBox.insertText(languageName);
            languageBox.setEditable(false);


            Button deleteButton = new Button(width, height + 20, 20, 20, new TextComponent("[X]"), (Button button) -> {
                ClientSideClientModService.getInstance().getClientEntity().getLanguages().stream().filter(l -> l.getIsoName().equals(language.getIsoName())).findFirst().ifPresent(l -> ClientSideClientModService.getInstance().getClientEntity().getLanguages().remove(l));
                String languageJson = new Gson().toJson(ClientSideClientModService.getInstance().getClientEntity().getLanguages());
                LanguagePacket translationKeyPackage = new LanguagePacket(languageJson);
                ModNetworking.CHANNEL.sendToServer(translationKeyPackage);

            });

            this.addRenderableWidget(deleteButton);
            this.addRenderableWidget(languageBox);

            height += 25;
        }

        //add language menu
        addLanguageInput(width, height);


        //controller
        addController(width, height);
    }

    private void addLanguageInput(int width, int height) {
        EditBox languageNameInput = new EditBox(this.font, width, height + 50, 100, 20, new TextComponent("Language Name"));
        EditBox languageISOInput = new EditBox(this.font, width + 100, height + 50, 50, 20, new TextComponent("ISO"));
        this.addRenderableWidget(languageNameInput);
        this.addRenderableWidget(languageISOInput);


        Button addButton = new Button(width + 150, height + 50, 20, 20, new TextComponent("+"), (Button button) -> {
            Language newLanguage = new Language(languageNameInput.getValue(), languageISOInput.getValue());
            ClientSideClientModService.getInstance().getClientEntity().getLanguages().add(newLanguage);
            String languageJson = new Gson().toJson(ClientSideClientModService.getInstance().getClientEntity().getLanguages());
            LanguagePacket languagePacket = new LanguagePacket(languageJson);
            ModNetworking.CHANNEL.sendToServer(languagePacket);
            renderMenu();
        });
        this.addRenderableWidget(addButton);
    }

    private void addController(int width, int height) {
        Button backButton = new Button(width + 100, height + 80, 20, 20, new TextComponent("<"), (Button button) -> {
            if (currentPage > 1) {
                currentPage--;
                renderMenu();
            }
        });
        this.addRenderableWidget(backButton);

        Button currentPageButton = new Button((width + 100) + 25, height + 80, 20, 20, new TextComponent(currentPage + "/" + totalPages), (Button button) -> {
        });
        this.addRenderableWidget(currentPageButton);

        Button forwardButton = new Button((width + 100) + 50, height + 80, 20, 20, new TextComponent(">"), (Button button) -> {
            if (totalPages > currentPage) {
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
