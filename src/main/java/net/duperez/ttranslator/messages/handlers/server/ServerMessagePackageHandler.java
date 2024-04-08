package net.duperez.ttranslator.messages.handlers.server;

import com.google.gson.Gson;
import net.duperez.ttranslator.entities.Message;
import net.duperez.ttranslator.network.ModNetworking;
import net.duperez.ttranslator.services.server.ServerUserService;
import net.duperez.ttranslator.entities.ServerUserEntity;
import net.duperez.ttranslator.messages.MessagePackage;
import net.duperez.ttranslator.services.common.ConfigService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static net.duperez.ttranslator.services.server.TranslationService.translateMessageMultiTread;

public class ServerMessagePackageHandler {

    public static void serverProcessMessage(ServerPlayer player, String message) throws IOException, ExecutionException, InterruptedException {
        ServerUserService serverUserService = ServerUserService.getInstance();
        if (player != null) {
            player.getName();

            if(ConfigService.getInstance().getConfigs().getTranslationKey() != null && !ConfigService.getInstance().getConfigs().getTranslationKey().equals("")) {
                ServerUserEntity user =  serverUserService.findUser(player.getName().getContents());


                Message msg = new Gson().fromJson(message, Message.class);

                List<Message> translatedMessages = new ArrayList<>(translateMessageMultiTread(msg, user.getReadLanguage()));

                System.out.println("translated the message: " + translatedMessages.size());

                translatedMessages.forEach(tMessage -> System.out.println(tMessage.getMessage()));

                ModNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new MessagePackage(new Gson().toJson(translatedMessages)));

            }
        }
    }
}
