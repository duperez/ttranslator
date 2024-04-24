package net.duperez.ttranslator.client.services;

import net.duperez.ttranslator.client.entities.ClientSideClientConfigs;
import net.duperez.ttranslator.common.services.interfaces.ClientServiceInterface;
import net.duperez.ttranslator.common.services.interfaces.SingletonInterface;

public class ClientSideClientLanguageService extends SingletonInterface implements ClientServiceInterface {

    public static ClientSideClientLanguageService instance;

    private ClientSideClientConfigs ClientEntity = new ClientSideClientConfigs();
    public static ClientSideClientLanguageService getInstance() {
        if (instance == null) {
            instance = new ClientSideClientLanguageService();
        }

        return instance;
    }


    public ClientSideClientConfigs getClientEntity() {
        return ClientEntity;
    }

    public void setClientEntity(ClientSideClientConfigs userEntityList) {
        this.ClientEntity = userEntityList;
    }


}
