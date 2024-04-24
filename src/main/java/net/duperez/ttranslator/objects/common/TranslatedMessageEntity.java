package net.duperez.ttranslator.objects.common;

public class TranslatedMessageEntity {

    String message;
    Language originalLanguage;

    public Language getTranslatedLanguage() {
        return TranslatedLanguage;
    }

    public void setTranslatedLanguage(Language translatedLanguage) {
        TranslatedLanguage = translatedLanguage;
    }

    Language TranslatedLanguage;
    String sender;



    public TranslatedMessageEntity(String message, Language originalLanguage, Language TranslatedLanguage, String sender) {
        this.message = message;
        this.originalLanguage = originalLanguage;
        this.TranslatedLanguage = TranslatedLanguage;
        this.sender = sender;
    }

    public TranslatedMessageEntity() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
