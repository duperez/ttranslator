package net.duperez.ttranslator.client.entities;

import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.objects.interfaces.ClientConfigsInterface;

import java.util.ArrayList;
import java.util.List;

public class ClientSideClientConfigs implements ClientConfigsInterface {

    String name;
    List<Language> readLanguage = new ArrayList<>();

    Language spokenLanguage;

    boolean isOp;

    public ClientSideClientConfigs() {

    }

    public boolean isOp() {
        return isOp;
    }

    public void setOp(boolean op) {
        isOp = op;
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
