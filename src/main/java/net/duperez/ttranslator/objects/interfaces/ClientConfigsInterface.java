package net.duperez.ttranslator.objects.interfaces;

import net.duperez.ttranslator.objects.common.Language;

import java.util.List;

public interface ClientConfigsInterface {

    public String getName();

    public void setName(String name);

    public List<Language> getReadLanguage();

    public void setReadLanguage(List<Language> readLanguage);

    public Language getSpokenLanguage();

    public void setSpokenLanguage(Language spokenLanguage);

}
