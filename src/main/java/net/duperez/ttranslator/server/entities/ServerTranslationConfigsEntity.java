package net.duperez.ttranslator.server.entities;

import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.objects.interfaces.ConfigsInterface;

import java.util.ArrayList;
import java.util.List;

public class ServerTranslationConfigsEntity implements ConfigsInterface {

    List<Language> languages = new ArrayList<>();

    String translationKey;


    @Override
    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages ;
    }

    @Override
    public void addLanguage(Language language) {
        if(languages.stream().filter(l -> l.getIsoName().equals(language.getIsoName())).toList().isEmpty())
            languages.add(language);
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }

}
