package com.example.ttranslator.server.events;

import com.example.ttranslator.Ttranslator;
import com.example.ttranslator.server.objects.player.PlayerInfo;
import com.example.ttranslator.server.objects.player.PlayerOnMemoryManager;
import com.example.ttranslator.server.objects.language.TranslatedMessage;
import com.example.ttranslator.server.service.TranslationService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Mod.EventBusSubscriber(modid = Ttranslator.MODID, value = Dist.DEDICATED_SERVER)
public class ChatEventHandler {

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) throws ExecutionException, InterruptedException {
        event.setCanceled(true);

        ServerPlayer sender = event.getPlayer();
        PlayerInfo senderInfo = PlayerOnMemoryManager.getInstance().getPlayerByUUID(sender.getStringUUID()).get();

        String originalMessage = event.getMessage().getString();

        sender.sendSystemMessage(Component.literal("<" + event.getPlayer().getName().getString() + "> " + originalMessage));

        //get a list of languages from read languages in PlayerOnMemoryManagement.getInstance().getPlayers()
        List<String> readLanguages = PlayerOnMemoryManager.getInstance().getPlayers().stream()
                .filter(PlayerInfo::isTranslating)
                .flatMap(player -> player.getReadLanguage().stream())
                .distinct()
                .filter(s -> !s.equals(senderInfo.getSpokenLanguage()))
                .toList();

        List<TranslatedMessage> translatedMessages = TranslationService.translateMessageMultiTread(originalMessage, senderInfo.getSpokenLanguage(), readLanguages);


        event.getPlayer().getServer().getPlayerList().getPlayers().stream().filter(s -> !s.getStringUUID().equals(sender.getStringUUID())).forEach(s -> {
            PlayerInfo receiverInfo = PlayerOnMemoryManager.getInstance().getPlayerByUUID(s.getStringUUID()).get();
            if(receiverInfo.getSpokenLanguage() == null || senderInfo.getSpokenLanguage().isEmpty() || receiverInfo.isShowOriginalMessages() ||receiverInfo.getReadLanguage().contains(senderInfo.getSpokenLanguage())) {
                s.sendSystemMessage(Component.literal("<" + event.getPlayer().getName().getString() + "> " + originalMessage));
            }

            translatedMessages.stream().filter(translatedMessage -> receiverInfo.getReadLanguage().contains(translatedMessage.getTranslatedLanguage()) && receiverInfo.isTranslating()).forEach(message -> s.sendSystemMessage(Component.literal("<" + event.getPlayer().getName().getString() + "> [" + message.getOriginalLanguage() + " -> " + message.getTranslatedLanguage() +  "]: " + message.getMessage())));
        });
    }
}