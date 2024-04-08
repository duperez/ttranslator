package net.duperez.ttranslator.events.client;

import com.google.gson.Gson;
import net.duperez.ttranslator.messages.LanguagePacket;
import net.duperez.ttranslator.network.ModNetworking;
import net.duperez.ttranslator.services.common.ConfigService;
import net.duperez.ttranslator.services.server.ServerUserService;
import net.duperez.ttranslator.entities.Configs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber
public class PlayerEvents {

    static ServerUserService serverUserService = ServerUserService.getInstance();

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // Aqui você pode criar e enviar seu pacote
        // Por exemplo, enviar um pacote para todos os jogadores informando que um novo jogador entrou
        //NetworkSetup.sendToAll(new MyCustomPacket("Bem-vindo ao servidor!"));

        // Ou, se você quiser enviar um pacote apenas para o jogador que entrou, use:

        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        //userService.addUser(new UserEntity(player.getName().getContents(), "", ""));

        Configs configs = ConfigService.getInstance().getConfigs();

        LanguagePacket packet = new LanguagePacket(new Gson().toJson(configs.getLanguages()));
        ModNetworking.CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        // Aqui você pode criar e enviar seu pacote
        // Por exemplo, enviar um pacote para todos os jogadores informando que um novo jogador entrou
        //NetworkSetup.sendToAll(new MyCustomPacket("Bem-vindo ao servidor!"));

        // Ou, se você quiser enviar um pacote apenas par a o jogador que entrou, use:

        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        Configs configs = ConfigService.getInstance().getConfigs();
    }
}