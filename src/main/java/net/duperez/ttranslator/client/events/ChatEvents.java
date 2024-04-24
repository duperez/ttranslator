package net.duperez.ttranslator.client.events;

import com.google.gson.Gson;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.common.messages.MessagePackage;
import net.duperez.ttranslator.common.network.ModNetworking;
import net.duperez.ttranslator.objects.common.ToTranslateMessageEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChatEvents {

    @SubscribeEvent
    public static void onPlayerChat(ClientChatEvent event) {
        System.out.println("player sent a message in the chat");
        System.out.println(event.getMessage());

        String playerName = Minecraft.getInstance().player.getName().getString();

        ToTranslateMessageEntity message = new ToTranslateMessageEntity(event.getMessage(), ClientSideClientLanguageService.getInstance().getClientEntity().getSpokenLanguage(), playerName);

        MessagePackage messagePackage = new MessagePackage(new Gson().toJson(message));

        ModNetworking.CHANNEL.sendToServer(messagePackage);
    }
}
