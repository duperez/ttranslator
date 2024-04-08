package net.duperez.ttranslator.entities;

import java.util.ArrayList;
import java.util.List;

public class UserEntity {

    public UserEntity() {

    }

    private Language spokenLanguage;
    private List<Language> readLanguage = new ArrayList<>();
    public Language getSpokenLanguage() {
        return spokenLanguage;
    }

    public void setSpokenLanguage(Language spokenLanguage) {
        this.spokenLanguage = spokenLanguage;
    }

    public void addReadLanguiage(Language spokenLanguage) {
        this.readLanguage.add(spokenLanguage);
    }

    public List<Language> getReadLanguage() {
        return readLanguage;
    }

    public void setReadLanguage(List<Language> readLanguage) {
        this.readLanguage = readLanguage;
    }
}
