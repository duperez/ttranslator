package net.duperez.ttranslator.client.gui.bkp;

import com.mojang.blaze3d.vertex.PoseStack;
import net.duperez.ttranslator.client.gui.MainScreen;
import net.duperez.ttranslator.client.gui.baseScreens.BaseTTranslatorScreen;
import net.duperez.ttranslator.common.messages.TranslationKeyPackage;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ServerKeyTranslationScreen extends BaseTTranslatorScreen {

    public ServerKeyTranslationScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        int width = this.width / 2 - 100;
        int height = this.height / 4 - 10;

        //addBackButton();
        super.addDefaultBackButton(new MainScreen(new TextComponent("Main menu")));

        EditBox textField = new EditBox(this.font, width, height, 200, 20, new TextComponent("Text Field"));
        textField.setMaxLength(40);
        textField.setValue("");
        addRenderableWidget(textField);

        this.addRenderableWidget(new Button(width, height + 30, 200, 20, new TextComponent("save translation key"), button -> {
            TranslationKeyPackage translationKeyPackage = new TranslationKeyPackage(textField.getValue());
            ModNetworking.CHANNEL.sendToServer(translationKeyPackage);
        }));

    }
}
