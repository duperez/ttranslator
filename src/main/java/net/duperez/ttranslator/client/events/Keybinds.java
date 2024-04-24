package net.duperez.ttranslator.client.events;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import net.duperez.ttranslator.client.gui.MainScreen;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Keybinds {

    private static final String CATEGORY = "key.categories.seumod";
    private static final KeyMapping openGuiKey = new KeyMapping("key.seumod.opengui", GLFW.GLFW_KEY_M, CATEGORY);

    public static void register() {
        ClientRegistry.registerKeyBinding(openGuiKey);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (openGuiKey.consumeClick()) {
            Minecraft.getInstance().setScreen(new MainScreen(new TextComponent("Custom GUI")));
        }
    }
}