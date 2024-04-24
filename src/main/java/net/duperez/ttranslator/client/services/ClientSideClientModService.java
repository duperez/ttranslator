package net.duperez.ttranslator.client.services;

import net.duperez.ttranslator.client.entities.ClientSideClientConfigs;
import net.duperez.ttranslator.client.entities.ClientTranslationConfigsEntity;
import net.duperez.ttranslator.common.services.interfaces.ClientServiceInterface;
import net.duperez.ttranslator.common.services.interfaces.SingletonInterface;

public class ClientSideClientModService extends SingletonInterface implements ClientServiceInterface {

    public static ClientSideClientModService instance;

    private ClientTranslationConfigsEntity ClientEntity = new ClientTranslationConfigsEntity();

    public static ClientSideClientModService getInstance() {
        if (instance == null) {
            instance = new ClientSideClientModService();
        }

        return instance;
    }


    public ClientTranslationConfigsEntity getClientEntity() {
        return ClientEntity;
    }

    public void setClientEntity(ClientTranslationConfigsEntity userEntityList) {
        this.ClientEntity = userEntityList;
    }


}
