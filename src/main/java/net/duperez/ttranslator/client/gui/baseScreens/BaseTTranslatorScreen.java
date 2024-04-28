package net.duperez.ttranslator.client.gui.baseScreens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class BaseTTranslatorScreen extends Screen {

    public static final int BUTTON_SELECTED_COLOR = 0xFFFF00; //yellow
    public static final int BUTTON_DEFAULT_COLOR = 0xFFFFFF; //white

    protected BaseTTranslatorScreen(Component pTitle) {
        super(pTitle);
    }

    public void addDefaultBackButton(Screen gotoScreen) {
        Button backButton = new Button(80, 10, 100, 20, new TextComponent("Back"), (Button button) -> super.getMinecraft().setScreen(gotoScreen));
        this.addRenderableWidget(backButton);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderDirtBackground(0);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
