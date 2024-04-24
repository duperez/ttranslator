package net.duperez.ttranslator.server.services;

import net.duperez.ttranslator.objects.interfaces.ConfigsInterface;
import net.duperez.ttranslator.server.entities.ServerTranslationConfigsEntity;
import net.duperez.ttranslator.common.services.interfaces.ConfigServiceInterface;
import net.duperez.ttranslator.common.services.interfaces.SingletonInterface;

public class ServerSideConfigsService extends SingletonInterface implements ConfigServiceInterface {

    static private ServerSideConfigsService instance;

    ServerTranslationConfigsEntity configs = new ServerTranslationConfigsEntity();

    public static ServerSideConfigsService getInstance() {
        if(instance == null) {
            instance = new ServerSideConfigsService();
        }
        return instance;
    }

    public ServerTranslationConfigsEntity getConfigs() {
        if(configs == null) {
            configs = new ServerTranslationConfigsEntity();
        }
        return configs;
    }

    public void setConfigs(ServerTranslationConfigsEntity configs) {
        this.configs = configs;
    }
}
