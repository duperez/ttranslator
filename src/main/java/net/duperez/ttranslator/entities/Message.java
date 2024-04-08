package net.duperez.ttranslator.entities;

public class Message {

    String sender;
    String message;
    Language newLanguage;
    Language originalLanguage;

    public Message(String message, Language newLanguage, Language originalLanguage, String sender) {
        this.message = message;
        this.newLanguage = newLanguage;
        this.sender = sender;
        this.originalLanguage = originalLanguage;
    }

    public String getMessage() {
        return message;
    }

    public Language getNewLanguage() {
        return newLanguage;
    }

    public String getSender() {
        return sender;
    }

    public Language getOriginalLanguage() {
        return originalLanguage;
    }

}
