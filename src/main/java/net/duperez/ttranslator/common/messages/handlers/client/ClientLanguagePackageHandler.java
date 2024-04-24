package net.duperez.ttranslator.common.messages.handlers.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.duperez.ttranslator.client.entities.ClientSideClientConfigs;
import net.duperez.ttranslator.client.entities.ClientTranslationConfigsEntity;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.client.services.ClientSideClientModService;
import net.duperez.ttranslator.common.services.bkp.client.ClientMessageService;
import net.duperez.ttranslator.objects.common.Language;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;

import java.lang.reflect.Type;
import java.util.List;

public class ClientLanguagePackageHandler {

    public static void playerProcessMessage(String message) {
        Gson gson = new Gson();


        //TODO: REMOVE
        Minecraft.getInstance().player.displayClientMessage(new TextComponent(message), false);
        Type languageListType = new TypeToken<List<Language>>() {
        }.getType();
        List<Language> languageList = gson.fromJson(message, languageListType);

        Minecraft.getInstance().player.displayClientMessage(new TextComponent(message), false);

        ClientTranslationConfigsEntity configs = ClientSideClientModService.getInstance().getClientEntity();
        configs.getLanguages().clear();
        languageList.forEach(configs::addLanguage);
        configs.getLanguages().forEach(language -> Minecraft.getInstance().player.displayClientMessage(new TextComponent(language.getIsoName()), false));
        if(ClientSideClientLanguageService.getInstance().getClientEntity() == null) {
            ClientSideClientConfigs clientSideClientConfigs = new ClientSideClientConfigs();
            clientSideClientConfigs.setName(Minecraft.getInstance().player.getName().getString());
            ClientSideClientLanguageService.getInstance().setClientEntity(clientSideClientConfigs);
        }
    }
}
