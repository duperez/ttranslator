package net.duperez.ttranslator.messages.handlers.server;

import com.google.gson.Gson;
import net.duperez.ttranslator.entities.UserEntity;
import net.duperez.ttranslator.services.server.ServerUserService;
import net.duperez.ttranslator.entities.ServerUserEntity;
import net.minecraft.server.level.ServerPlayer;

public class ServerClientPackageHandler {

    public static void serverProcessMessage(ServerPlayer player, String message) {
        ServerUserService serverUserService = ServerUserService.getInstance();
        if (player != null) {

            System.out.println("user sent new configs: " + player.getName().getString());

            ServerUserEntity user =  serverUserService.findUser(player.getName().getString());

            UserEntity uEntity = new Gson().fromJson(message, UserEntity.class);

            ServerUserEntity serverUserEntity = new ServerUserEntity(player.getName().getString(), uEntity.getReadLanguage(), uEntity.getSpokenLanguage());

            if(user != null) {
                serverUserService.removeUser(player.getName().getString());
            }

            serverUserService.addUser(serverUserEntity);
        }
    }






}
