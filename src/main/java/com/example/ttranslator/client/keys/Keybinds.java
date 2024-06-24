package com.example.ttranslator.client.keys;

import com.example.ttranslator.Ttranslator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = Ttranslator.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Keybinds {
    public static final KeyMapping TRANSLATE_SIGN_KEY = new KeyMapping(
            "sign translation key", // Nome da tecla
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y, // Tecla Y
            "key.categories.misc" // Categoria
    );

    public static final KeyMapping SIGN_TTS_KEY = new KeyMapping(
            "sign TTS key", // Nome da tecla
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U, // Tecla U
            "key.categories.misc" // Categoria
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TRANSLATE_SIGN_KEY);
        event.register(SIGN_TTS_KEY);
    }

    @Mod.EventBusSubscriber(modid = Ttranslator.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class KeyInputHandler {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (TRANSLATE_SIGN_KEY.consumeClick()) {
                // Executar o comando
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.connection.sendCommand("translate sign");
                }
            }
            if (SIGN_TTS_KEY.consumeClick()) {
                // Executar o comando
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.connection.sendCommand("TTS");
                }
            }
        }
    }
}