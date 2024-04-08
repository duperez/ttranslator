package net.duperez.ttranslator.events.client;

import com.google.gson.Gson;
import net.duperez.ttranslator.entities.UserEntity;
import net.duperez.ttranslator.network.ModNetworking;
import net.duperez.ttranslator.messages.UserPackage;
import net.duperez.ttranslator.services.client.ClientUserService;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientDataSaver {

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggedInEvent event) {
        // Este método será chamado quando o próprio jogador entrar no servidor
        System.out.println("Entrou no servidor.");

        UserEntity user;

        String path = "ttranslator/translation.json";
        if (Files.exists(Paths.get(path))) {
            try (FileReader reader = new FileReader(path)) {
                Gson gson = new Gson();
                user = gson.fromJson(reader, UserEntity.class);

                ClientUserService.getInstance().setUser(user);

                UserPackage userPackage = new UserPackage(new Gson().toJson(user));

                ModNetworking.CHANNEL.sendToServer(userPackage);
                // Agora myData contém seus dados carregados. Você pode usá-los conforme necessário.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggedOutEvent event) throws IOException {
        // Este método será chamado quando o próprio jogador sair do servidor
        System.out.println("Saiu do servidor.");

        Gson gson = new Gson();

        String json = gson.toJson(ClientUserService.getInstance().getUser());

        Files.createDirectories(Paths.get("ttranslator"));

        // Substitua "data.json" pelo caminho do arquivo onde você deseja salvar os dados.
        try (FileWriter writer = new FileWriter("ttranslator/translation.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}