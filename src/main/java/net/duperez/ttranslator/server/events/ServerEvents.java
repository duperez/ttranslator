package net.duperez.ttranslator.server.events;

import com.google.gson.Gson;
import net.duperez.ttranslator.common.messages.LanguagePacket;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.server.entities.ServerTranslationConfigsEntity;
import net.duperez.ttranslator.server.services.ServerSideClientService;
import net.duperez.ttranslator.server.services.ServerSideConfigsService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber
public class ServerEvents {
    private static final Gson gson = new Gson();

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        sendLanguagePacketToClient(player);
    }

    private static void sendLanguagePacketToClient(ServerPlayer player) {
        ServerTranslationConfigsEntity configs = ServerSideConfigsService.getInstance().getConfigs();
        String languageJson = gson.toJson(configs.getLanguages());
        LanguagePacket packet = new LanguagePacket(languageJson);

        ModNetworking.CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        ServerSideClientService.getInstance().removeClient(player.getName().getString());
    }
}