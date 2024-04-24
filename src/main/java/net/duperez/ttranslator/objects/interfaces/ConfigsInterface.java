package net.duperez.ttranslator.objects.interfaces;

import net.duperez.ttranslator.objects.common.Language;

import java.util.List;

public interface ConfigsInterface {

    public List<Language> getLanguages();

    public void addLanguage(Language language);

}
