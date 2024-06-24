package com.example.ttranslator.server.objects.language;

public class TranslatedMessage {
    String message;
    String originalLanguage;
    String translatedLanguage;

    public TranslatedMessage(String message, String originalLanguage, String translatedLanguage) {
        this.message = message;
        this.originalLanguage = originalLanguage;
        this.translatedLanguage = translatedLanguage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTranslatedLanguage() {
        return translatedLanguage;
    }

    public void setTranslatedLanguage(String translatedLanguage) {
        this.translatedLanguage = translatedLanguage;
    }
}
