package net.duperez.ttranslator.common.services.bkp.client;

import net.duperez.ttranslator.client.entities.ClientTranslationUiConfigEntity;
import net.duperez.ttranslator.client.services.ClientSideUiConfigsService;
import net.duperez.ttranslator.objects.common.TranslatedMessageEntity;

@Deprecated
public class ClientMessageService {


    public static String buildMessage(TranslatedMessageEntity message) {
        ClientTranslationUiConfigEntity clientConfig = ClientSideUiConfigsService.getInstance().getConfigs();
        //build language prefix
        String prefix = "";
        if (clientConfig.isShowOriginalLanguage() && clientConfig.isShowTranslatedLanguage()) {
            prefix = "[" + message.getOriginalLanguage().getIsoName() + "->" + message.getTranslatedLanguage().getIsoName() + "] ";
        } else if (clientConfig.isShowOriginalLanguage()) {
            prefix = "[" + message.getOriginalLanguage().getIsoName() + "] ";
        } else if (clientConfig.isShowTranslatedLanguage()) {
            prefix = "[" + message.getTranslatedLanguage().getIsoName() + "] ";
        }

        return prefix + message.getMessage();
    }
}