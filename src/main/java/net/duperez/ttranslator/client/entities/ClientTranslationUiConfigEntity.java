package net.duperez.ttranslator.client.entities;

public class ClientTranslationUiConfigEntity {

    boolean showTranslations = true;
    boolean showOriginalMessage = true;
    boolean showOwnTranslations = true;
    boolean showOriginalLanguage = true;
    boolean showTranslatedLanguage = true;

    public boolean isShowTranslations() {
        return showTranslations;
    }

    public void setShowTranslations(boolean showTranslations) {
        this.showTranslations = showTranslations;
    }

    public boolean isShowOwnTranslations() {
        return showOwnTranslations;
    }

    public void setShowOwnTranslations(boolean showOwnTranslations) {
        this.showOwnTranslations = showOwnTranslations;
    }

    public boolean isShowOriginalLanguage() {
        return showOriginalLanguage;
    }

    public void setShowOriginalLanguage(boolean showOriginalLanguage) {
        this.showOriginalLanguage = showOriginalLanguage;
    }

    public boolean isShowTranslatedLanguage() {
        return showTranslatedLanguage;
    }

    public void setShowTranslatedLanguage(boolean showTranslatedLanguage) {
        this.showTranslatedLanguage = showTranslatedLanguage;
    }

    public boolean isShowOriginalMessage() {
        return showOriginalMessage;
    }

    public void setShowOriginalMessage(boolean showOriginalMessage) {
        this.showOriginalMessage = showOriginalMessage;
    }


}
