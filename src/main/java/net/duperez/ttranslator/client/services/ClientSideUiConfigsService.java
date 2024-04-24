package net.duperez.ttranslator.client.services;

import net.duperez.ttranslator.client.entities.ClientTranslationUiConfigEntity;
import net.duperez.ttranslator.common.services.interfaces.ConfigServiceInterface;
import net.duperez.ttranslator.common.services.interfaces.SingletonInterface;

public class ClientSideUiConfigsService extends SingletonInterface implements ConfigServiceInterface {

    static private ClientSideUiConfigsService instance;

    ClientTranslationUiConfigEntity configs = new ClientTranslationUiConfigEntity();

    public static ClientSideUiConfigsService getInstance() {
        if(instance == null) {
            instance = new ClientSideUiConfigsService();
        }
        return instance;
    }

    public ClientTranslationUiConfigEntity getConfigs() {
        return configs;
    }

    public void setConfigs(ClientTranslationUiConfigEntity configs) {
        this.configs = configs;
    }
}
