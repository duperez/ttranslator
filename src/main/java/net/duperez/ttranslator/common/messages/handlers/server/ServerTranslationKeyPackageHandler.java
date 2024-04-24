package net.duperez.ttranslator.common.messages.handlers.server;

import net.duperez.ttranslator.server.services.ServerSideConfigsService;

public class ServerTranslationKeyPackageHandler {

    public static void serverProcessMessage(String message) {
        ServerSideConfigsService.getInstance().getConfigs().setTranslationKey(message);
    }
}
