package net.duperez.ttranslator.services.client;

import net.duperez.ttranslator.entities.ClientConfig;
import net.duperez.ttranslator.entities.Message;

public class ClientMessageService {


    public static String buildMessage(Message message) {
        ClientConfig clientConfig = ClientUserService.getInstance().getClientConfig();
        //build language prefix
        String prefix = "";
        if(clientConfig.isShowOriginalLanguage() && clientConfig.isShowTranslatedLanguage()) {
            prefix = "[" + message.getOriginalLanguage().getSmName() + "->" + message.getNewLanguage().getSmName() + "] ";
        } else if(clientConfig.isShowOriginalLanguage()) {
            prefix = "[" + message.getOriginalLanguage().getSmName() + "] ";
        } else if(clientConfig.isShowTranslatedLanguage()) {
            prefix = "[" + message.getNewLanguage().getSmName() + "] ";
        }

        //return message

        return prefix + message.getMessage();
    }



}
