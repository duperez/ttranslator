package net.duperez.ttranslator.messages.handlers.server;

import net.duperez.ttranslator.services.common.ConfigService;

public class ServerTranslationKeyPackageHandler {

    public static void serverProcessMessage(String message) {
        ConfigService.getInstance().getConfigs().setTranslationKey(message);
    }
}
