package com.example.ttranslator.server.objects.language;

import com.example.ttranslator.server.objects.database.DataFile;
import java.io.IOException;
import java.util.List;

public class LanguageManager extends DataFile<Language> {
    List<Language> languages;
    private static LanguageManager instance;

    private LanguageManager() {
        languages = loadDataList("translatorLanguage.json");
        if(languages.isEmpty()) {
            addLanguage(new Language( "English", "en", "en-US"));
            addLanguage(new Language( "Spanish", "es", "fr-FR"));
            addLanguage(new Language( "Portuguese", "pt", "pt-BR"));
            addLanguage(new Language( "French", "fr", "es-ES"));
        }
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void addLanguage(Language language) {
        languages.add(language);
    }

    public void removeLanguage(Language language) {
        languages.remove(language);
    }

    public void saveData() throws IOException {
        super.saveData("translatorLanguage.json", languages);
    }

    public void reloadLanguages() {
        languages = loadDataList("translatorLanguage.json");
    }

}
