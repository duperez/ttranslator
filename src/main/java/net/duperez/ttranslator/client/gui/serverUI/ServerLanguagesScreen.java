package net.duperez.ttranslator.client.gui.serverUI;

import com.google.gson.Gson;
import net.duperez.ttranslator.client.gui.MainScreen;
import net.duperez.ttranslator.client.gui.baseScreens.BaseTTranslatorPaginatedScreen;
import net.duperez.ttranslator.client.services.ClientSideClientModService;
import net.duperez.ttranslator.common.messages.LanguagePacket;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.objects.common.Language;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class ServerLanguagesScreen extends BaseTTranslatorPaginatedScreen {

    List<Language> languages;

    EditBox languageNameInput;
    EditBox languageIsoNameInput;
    Button addButton;

    public ServerLanguagesScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void init() {
        super.init();
        renderMenu();
    }

    protected void renderMenu() {
        this.clearWidgets();
        int width = this.width / 2 - 100;
        int height = this.height / 4 - 10;

        int startIndex = (currentPage - 1) * 4;

        //addBackButton();
        super.addDefaultBackButton(new MainScreen(new TextComponent("Main menu")));


        languages = new ArrayList<>(ClientSideClientModService.getInstance().getClientEntity().getLanguages());

        totalPages = getTotalPages();

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
                renderMenu();
            });

            this.addRenderableWidget(deleteButton);
            this.addRenderableWidget(languageBox);

            height += 25;
        }

        //add language menu
        addLanguageInput(width, height);


        //controller
        addDefaultPaginationController();
    }

    private void addLanguageInput(int width, int height) {
        EditBox languageNameInput = new EditBox(this.font, width, height + 50, 100, 20, new TextComponent("Language Name"));
        EditBox languageISOInput = new EditBox(this.font, width + 100, height + 50, 50, 20, new TextComponent("ISO"));
        languageNameInput.setSuggestion("English");
        languageISOInput.setSuggestion("EN");
        this.languageNameInput = languageNameInput;
        this.languageIsoNameInput = languageISOInput;
        this.addRenderableWidget(languageNameInput);
        this.addRenderableWidget(languageISOInput);
        this.languageNameInput.setResponder(i -> onTextChanged(languageNameInput, i, "English"));
        this.languageIsoNameInput.setResponder(i -> onTextChanged(languageIsoNameInput, i, "EN"));


        Button addButton = new Button(width + 150, height + 50, 20, 20, new TextComponent("+"), (Button button) -> {
            Language newLanguage = new Language(languageNameInput.getValue().toLowerCase(), languageISOInput.getValue().toLowerCase());
            ClientSideClientModService.getInstance().getClientEntity().getLanguages().add(newLanguage);
            String languageJson = new Gson().toJson(ClientSideClientModService.getInstance().getClientEntity().getLanguages());
            LanguagePacket languagePacket = new LanguagePacket(languageJson);
            ModNetworking.CHANNEL.sendToServer(languagePacket);
            renderMenu();
        });
        this.addRenderableWidget(addButton);
        this.addButton = addButton;
        this.addButton.active = false;
    }

    private void onTextChanged(EditBox edt, String text, String suggestionText) {
        // Se o campo de texto está vazio, mostra o texto de sugestão.
        edt.setSuggestion(text.isEmpty() ? suggestionText : "");

        this.addButton.active  = (!this.languageNameInput.getValue().isEmpty() && !this.languageIsoNameInput.getValue().isEmpty());
    }

}
