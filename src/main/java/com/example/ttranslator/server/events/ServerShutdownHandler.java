package com.example.ttranslator.server.events;

import com.example.ttranslator.Ttranslator;
import com.example.ttranslator.server.objects.language.LanguageManager;
import com.example.ttranslator.server.objects.player.PlayerOnFileManager;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Ttranslator.MODID)
public class ServerShutdownHandler {

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) throws IOException {
        PlayerOnFileManager.getInstance().saveData();
        LanguageManager.getInstance().saveData();
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        PlayerOnFileManager.getInstance();
        LanguageManager.getInstance();
    }
}