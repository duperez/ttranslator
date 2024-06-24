package com.example.ttranslator.server.events;

import com.example.ttranslator.Ttranslator;
import com.example.ttranslator.server.objects.player.PlayerOnMemoryManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Ttranslator.MODID, value = Dist.DEDICATED_SERVER)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // Este método é chamado sempre que um jogador entra no servidor
        if (event.getEntity() != null) {
            Player player = event.getEntity();
            // Aqui você pode executar seus métodos personalizados
            String translatorHelloMessage =
                    """
                    This server is using translator V.1 BETA mod and some features are disabled.
                    To use the translator in this version, use in-line commands:
                    /translate help
                    """;

            player.sendSystemMessage(Component.literal(translatorHelloMessage));

            PlayerOnMemoryManager.getInstance().loadPlayer(event.getEntity().getStringUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerOnMemoryManager.getInstance().removePlayer(PlayerOnMemoryManager.getInstance().getPlayerByUUID(event.getEntity().getStringUUID()).get());
    }

}