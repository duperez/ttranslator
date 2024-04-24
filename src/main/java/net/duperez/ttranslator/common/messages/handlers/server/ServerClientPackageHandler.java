package net.duperez.ttranslator.common.messages.handlers.server;

import com.google.gson.Gson;
import net.duperez.ttranslator.client.entities.ClientSideClientConfigs;
import net.duperez.ttranslator.server.entities.ServerSideClientConfigs;
import net.duperez.ttranslator.server.services.ServerSideClientService;
import net.minecraft.server.level.ServerPlayer;

public class ServerClientPackageHandler {
    private static final Gson GSON = new Gson();

    public static void serverProcessClientInfo(ServerPlayer player, String message) {
        ServerSideClientService serverUserService = ServerSideClientService.getInstance();
        String clientName = player.getName().getString();
        ServerSideClientConfigs existingUser = serverUserService.findUser(clientName);

        ClientSideClientConfigs clientConfigs = GSON.fromJson(message, ClientSideClientConfigs.class);

        ServerSideClientConfigs serverUserEntity = new ServerSideClientConfigs(
                clientName,
                clientConfigs.getReadLanguage(),
                clientConfigs.getSpokenLanguage()
        );

        if (existingUser != null) {
            serverUserService.removeClient(clientName);
        }

        serverUserService.addUser(serverUserEntity);
    }
}