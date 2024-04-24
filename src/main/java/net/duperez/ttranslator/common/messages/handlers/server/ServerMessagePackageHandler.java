package net.duperez.ttranslator.common.messages.handlers.server;

import com.google.gson.Gson;
import net.duperez.ttranslator.common.messages.MessagePackage;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.objects.common.ToTranslateMessageEntity;
import net.duperez.ttranslator.objects.common.TranslatedMessageEntity;
import net.duperez.ttranslator.server.entities.ServerSideClientConfigs;
import net.duperez.ttranslator.server.services.ServerSideClientService;
import net.duperez.ttranslator.server.services.ServerSideConfigsService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static net.duperez.ttranslator.server.services.TranslationService.translateMessageMultiTread;

public class ServerMessagePackageHandler {

    public static void serverProcessMessage(ServerPlayer player, String message) throws IOException, ExecutionException, InterruptedException {
        ServerSideClientService serverUserService = ServerSideClientService.getInstance();

        if (player != null) {
            player.getName();

            if (ServerSideConfigsService.getInstance().getConfigs().getTranslationKey() != null && !ServerSideConfigsService.getInstance().getConfigs().getTranslationKey().equals("")) {

                List<Language> toTranslateLanguages = serverUserService.findAllCurrentLanguages();

                ToTranslateMessageEntity msg = new Gson().fromJson(message, ToTranslateMessageEntity.class);

                List<TranslatedMessageEntity> translatedMessages = new ArrayList<>(translateMessageMultiTread(msg, toTranslateLanguages));

                translatedMessages.forEach(tMessage -> System.out.println(tMessage.getMessage()));

                ModNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new MessagePackage(new Gson().toJson(translatedMessages)));
            }
        }
    }
}