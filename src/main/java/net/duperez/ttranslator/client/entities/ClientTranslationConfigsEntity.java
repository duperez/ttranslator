package net.duperez.ttranslator.client.entities;

import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.objects.interfaces.ConfigsInterface;

import java.util.ArrayList;
import java.util.List;

public class ClientTranslationConfigsEntity implements ConfigsInterface {

    List<Language> languages = new ArrayList<>();


    @Override
    public List<Language> getLanguages() {
        return languages;
    }

    @Override
    public void addLanguage(Language language) {
        if(languages.stream().filter(l -> l.getIsoName().equals(language.getIsoName())).toList().isEmpty())
            languages.add(language);
    }
}
