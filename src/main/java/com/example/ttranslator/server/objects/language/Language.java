package com.example.ttranslator.server.objects.language;


public class Language {
    String name;
    String acronym;
    String ttsAcronym;
    public Language(String name, String acronym, String ttsAcronym) {
        this.name = name;
        this.acronym = acronym;
        this.ttsAcronym = ttsAcronym;
    }
    public String getName() {
        return name;
    }
    public String getAcronym() {
        return acronym;
    }
    public String getTtsAcronym() {
        return ttsAcronym;
    }
}
