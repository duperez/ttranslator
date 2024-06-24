package com.example.ttranslator.server.objects.player;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {

    String UUID;
    String spokenLanguage;
    List<String> readLanguage = new ArrayList<>();
    boolean isTranslating;
    boolean showOriginalMessages;
    String name;


    public PlayerInfo(String UUID) {
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getSpokenLanguage() {
        return spokenLanguage;
    }

    public void setSpokenLanguage(String spokenLanguage) {
        this.spokenLanguage = spokenLanguage;
    }

    public List<String> getReadLanguage() {
        return readLanguage;
    }

    public void setReadLanguage(List<String> readLanguage) {
        this.readLanguage = readLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTranslating() {
        return isTranslating;
    }

    public void setTranslating(boolean translating) {
        isTranslating = translating;
    }

    public boolean isShowOriginalMessages() {
        return showOriginalMessages;
    }

    public void setShowOriginalMessages(boolean showOriginalMessages) {
        this.showOriginalMessages = showOriginalMessages;
    }
}
