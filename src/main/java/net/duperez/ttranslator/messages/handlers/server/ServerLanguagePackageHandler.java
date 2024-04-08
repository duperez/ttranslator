package net.duperez.ttranslator.messages.handlers.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.duperez.ttranslator.entities.Language;
import net.duperez.ttranslator.services.common.ConfigService;
import net.duperez.ttranslator.entities.Configs;

import java.lang.reflect.Type;
import java.util.List;

public class ServerLanguagePackageHandler {

    public static void playerProcessMessage(String message) {
        Gson gson = new Gson();
        Type languageListType = new TypeToken<List<Language>>() {
        }.getType();
        List<Language> languageList = gson.fromJson(message, languageListType);
        Configs configs = ConfigService.getInstance().getConfigs();
        configs.getLanguages().clear();
        languageList.forEach(configs::addLanguage);
    }
}
