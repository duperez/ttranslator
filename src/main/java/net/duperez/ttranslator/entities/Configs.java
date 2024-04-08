package net.duperez.ttranslator.entities;

import java.util.ArrayList;
import java.util.List;

public class Configs {


    List<Language> languages = new ArrayList<>();

    String translationKey;


    public Configs() {

    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void addLanguage(Language language) {
        if(languages.stream().filter(l -> l.getSmName().equals(language.getSmName())).toList().isEmpty())
            languages.add(language);
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }


}
