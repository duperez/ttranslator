package net.duperez.ttranslator.messages.handlers.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.duperez.ttranslator.entities.Configs;
import net.duperez.ttranslator.entities.Language;
import net.duperez.ttranslator.services.common.ConfigService;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;

import java.lang.reflect.Type;
import java.util.List;

public class ClientLanguagePackageHandler {

    public static void playerProcessMessage(String message) {
        Gson gson = new Gson();
        Type languageListType = new TypeToken<List<Language>>() {
        }.getType();
        List<Language> languageList = gson.fromJson(message, languageListType);
        Configs configs = ConfigService.getInstance().getConfigs();
        languageList.forEach(configs::addLanguage);
        languageList.forEach(language -> Minecraft.getInstance().player.displayClientMessage(new TextComponent(language.getSmName()), false));
    }
}
