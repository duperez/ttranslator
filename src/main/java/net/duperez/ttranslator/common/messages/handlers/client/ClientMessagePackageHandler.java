package net.duperez.ttranslator.common.messages.handlers.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.client.services.ClientSideUiConfigsService;
import net.duperez.ttranslator.common.services.bkp.client.ClientMessageService;
import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.objects.common.TranslatedMessageEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class ClientMessagePackageHandler {

    public static void playerProcessMessage(String message) {
        Type messageListType = new TypeToken<List<TranslatedMessageEntity>>() {
        }.getType();
        List<TranslatedMessageEntity> messageList = new Gson().fromJson(message, messageListType);


        List<String> isos = ClientSideClientLanguageService.getInstance().getClientEntity().getReadLanguage().stream().map(Language::getIsoName).toList();
        if (ClientSideUiConfigsService.getInstance().getConfigs().isShowTranslations()) {
            messageList.stream().filter(msg ->  isos.contains(msg.getOriginalLanguage().getIsoName()))
                    .forEach(translatedMsg -> Minecraft.getInstance().player.displayClientMessage(new TextComponent(ClientMessageService.buildMessage(translatedMsg)), false));
        }
    }
}