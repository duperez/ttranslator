package net.duperez.ttranslator.common.messages.handlers.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.duperez.ttranslator.common.messages.LanguagePacket;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.server.services.ServerSideConfigsService;
import net.minecraftforge.network.PacketDistributor;

import java.lang.reflect.Type;
import java.util.List;

public class ServerLanguagePackageHandler {

    public static void serverProcessMessage(String message) {
        Gson gson = new Gson();
        Type languageListType = new TypeToken<List<Language>>() {
        }.getType();
        List<Language> languageList = gson.fromJson(message, languageListType);
        ServerSideConfigsService.getInstance().getConfigs().setLanguages(languageList);
        languageList.forEach(l -> ServerSideConfigsService.getInstance().getConfigs().addLanguage(l));
        String languageJson = gson.toJson(ServerSideConfigsService.getInstance().getConfigs().getLanguages());
        LanguagePacket packet = new LanguagePacket(languageJson);

        ModNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), packet);

    }
}
