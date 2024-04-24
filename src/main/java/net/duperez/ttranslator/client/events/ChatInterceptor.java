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
        String message = event.getMessage().getString();
        
        // Verifique se a mensagem atende a algum critério para ser cancelada
        if (!ClientSideUiConfigsService.getInstance().getConfigs().isShowOriginalMessage()) {
            event.setCanceled(true); // Cancela a mensagem, impedindo que ela apareça no chat
        }
    }
    
    private static boolean deveCancelarMensagem(String mensagem) {
        // Adicione sua lógica aqui para determinar se a mensagem deve ser cancelada
        // Exemplo: cancelar mensagens que contêm uma palavra específica
        return mensagem.contains("palavraProibida");
    }
}