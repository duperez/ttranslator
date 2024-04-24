package net.duperez.ttranslator.objects.common;

public class ToTranslateMessageEntity {

    String message;
    Language language;
    String sender;


    public ToTranslateMessageEntity(String message, Language language, String sender) {
        this.message = message;
        this.language = language;
        this.sender = sender;
    }

    public ToTranslateMessageEntity() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
