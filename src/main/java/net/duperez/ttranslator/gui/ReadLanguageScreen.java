package net.duperez.ttranslator.gui;

import com.google.gson.Gson;
import com.mojang.blaze3d.vertex.PoseStack;
import net.duperez.ttranslator.entities.Language;
import net.duperez.ttranslator.entities.UserEntity;
import net.duperez.ttranslator.messages.UserPackage;
import net.duperez.ttranslator.network.ModNetworking;
import net.duperez.ttranslator.services.client.ClientUserService;
import net.duperez.ttranslator.services.common.ConfigService;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ReadLanguageScreen extends Screen {

    ClientUserService userService = ClientUserService.getInstance();


    public ReadLanguageScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        int width = this.width / 2 - 100; // Centraliza os botões na tela
        int height = this.height / 4 - 10;

        int plusheight = 0;


        for (Language language : ConfigService.getInstance().getConfigs().getLanguages()) {
            addButton(language.getName(), width, height + plusheight, button -> selectLanguage(button, language), language);
            plusheight += 24;

        }
    }

    private void addButton(String label, int x, int y, Button.OnPress onPress, Language language) {
        Button button = new Button(x, y, 200, 20, new TextComponent(label), onPress);
        System.out.println();
        if (userService.getReadLanguage().stream().map(Language::getSmName).toList().contains(language.getSmName())) {
            button.setFGColor(0xFFFF00); //0xFFFF00
        } else {
            button.setFGColor(0xFFFFFF); //0xFFFFFF
        }
        this.addRenderableWidget(button);
    }

    private void selectLanguage(Button button, Language language) {
        if (button.getFGColor() == 0xFFFFFF) { //0xFFFFFF
            userService.getReadLanguage().add(language);
            button.setFGColor(0xFFFF00);
        } else {
            userService.getReadLanguage().remove(language);
            button.setFGColor(0xFFFFFF); //0xFFFFFF
        }

        UserEntity user = userService.getUser();

        UserPackage userPackage = new UserPackage(new Gson().toJson(user));

        ModNetworking.CHANNEL.sendToServer(userPackage);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack); // Isso vai desenhar o fundo escuro padrão
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}