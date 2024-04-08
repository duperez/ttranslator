package net.duperez.ttranslator.messages.handlers.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.duperez.ttranslator.entities.ClientConfig;
import net.duperez.ttranslator.entities.Message;
import net.duperez.ttranslator.services.client.ClientMessageService;
import net.duperez.ttranslator.services.client.ClientUserService;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;

import java.lang.reflect.Type;
import java.util.List;

public class ClientMessagePackageHandler {

    public static void playerProcessMessage(String message) {
        Type messageListType = new TypeToken<List<Message>>(){}.getType();
        List<Message> messageList = new Gson().fromJson(message, messageListType);
        ClientConfig clientConfig = ClientUserService.getInstance().getClientConfig();
        if(clientConfig.isShowTranslations() && !(!clientConfig.isShowOwnTranslations() && messageList.get(0).getSender().equals(Minecraft.getInstance().player.getName().getString()))) {
            messageList.forEach(translatedMsg -> Minecraft.getInstance().player.displayClientMessage(new TextComponent(ClientMessageService.buildMessage(translatedMsg)), false));
        }
    }






}
