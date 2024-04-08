package net.duperez.ttranslator.services.common;

import net.duperez.ttranslator.entities.Configs;

public class ConfigService {

    private static ConfigService instance;

    private Configs configs = new Configs();

    public static ConfigService getInstance() {
        if (instance == null) {
            instance = new ConfigService();
        }
        return instance;
    }

    public void loadConfigs(Configs configs) {
        this.configs = configs;
    }

    public Configs getConfigs() {
        return configs;
    }

    public void setConfigs(Configs configs) {
        this.configs = configs;
    }
}
