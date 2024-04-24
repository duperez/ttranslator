package net.duperez.ttranslator.server.entities;

import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.objects.interfaces.ClientConfigsInterface;

import java.util.ArrayList;
import java.util.List;

public class ServerSideClientConfigs implements ClientConfigsInterface {

    String name;
    List<Language> readLanguage = new ArrayList<>();

    Language spokenLanguage;

    public ServerSideClientConfigs(String name, List<Language> readLanguage, Language spokenLanguage) {
        this.name = name;
        this.readLanguage = readLanguage;
        this.spokenLanguage = spokenLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Language> getReadLanguage() {
        return readLanguage;
    }

    public void setReadLanguage(List<Language> readLanguage) {
        this.readLanguage = readLanguage;
    }

    public Language getSpokenLanguage() {
        return spokenLanguage;
    }

    public void setSpokenLanguage(Language spokenLanguage) {
        this.spokenLanguage = spokenLanguage;
    }


}
