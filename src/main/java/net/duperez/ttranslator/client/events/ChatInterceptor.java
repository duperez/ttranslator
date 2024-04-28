package net.duperez.ttranslator.client.events;

import net.duperez.ttranslator.client.services.ClientSideUiConfigsService;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ChatInterceptor {

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        event.setCanceled(!ClientSideUiConfigsService.getInstance().getConfigs().isShowOriginalMessage()); // Cancela a mensagem, impedindo que ela apareça no chat
    }
}