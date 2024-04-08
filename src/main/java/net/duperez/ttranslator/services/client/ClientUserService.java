package net.duperez.ttranslator.services.client;

import net.duperez.ttranslator.entities.ClientConfig;
import net.duperez.ttranslator.entities.Language;
import net.duperez.ttranslator.entities.UserEntity;

import java.util.List;

public class ClientUserService {

    public static ClientUserService instance;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    private UserEntity user;

    private ClientConfig clientConfig = new ClientConfig();

    public static ClientUserService getInstance() {
        if(instance == null) {
            instance = new ClientUserService();
        }

        return instance;
    }

    private ClientUserService() {
        user = new UserEntity();
    }

    public void changeSpokenLanguage(Language language) {
        user.setSpokenLanguage(language);
    }

    public void addReadLanguage(Language language) {
        user.getReadLanguage().add(language);
    }

    public void removeReadLangauge(String language) {
        user.getReadLanguage().removeIf(language1 -> language1.getSmName().equals(language));
    }

    public List<Language> getReadLanguage() {
        return user.getReadLanguage();
    }

    public Language getSpokenLanguage() {
        return user.getSpokenLanguage();
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

}
