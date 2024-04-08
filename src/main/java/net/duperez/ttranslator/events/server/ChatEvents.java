package net.duperez.ttranslator.events.server;

import com.google.gson.Gson;
import net.duperez.ttranslator.entities.Message;
import net.duperez.ttranslator.messages.MessagePackage;
import net.duperez.ttranslator.network.ModNetworking;
import net.duperez.ttranslator.services.client.ClientUserService;
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

        Message message = new Message(event.getMessage(), ClientUserService.getInstance().getSpokenLanguage(), ClientUserService.getInstance().getSpokenLanguage(), playerName);

        MessagePackage messagePackage = new MessagePackage(new Gson().toJson(message));

        ModNetworking.CHANNEL.sendToServer(messagePackage);
    }
}
