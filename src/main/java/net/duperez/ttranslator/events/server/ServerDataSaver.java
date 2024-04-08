package net.duperez.ttranslator.events.server;

import com.google.gson.Gson;
import net.duperez.ttranslator.entities.Configs;
import net.duperez.ttranslator.services.common.ConfigService;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod.EventBusSubscriber
public class ServerDataSaver {

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) throws IOException {
        // Substitua MyDataClass pela classe que contém os dados que você deseja salvar.
        System.out.println("saving config data");
        Gson gson = new Gson();
        if (ConfigService.getInstance().getConfigs().getTranslationKey() == null) {
            Configs configs = ConfigService.getInstance().getConfigs();
            configs.setTranslationKey("");
        }

        String json = gson.toJson(ConfigService.getInstance().getConfigs());

        Files.createDirectories(Paths.get("ttranslator"));

        // Substitua "data.json" pelo caminho do arquivo onde você deseja salvar os dados.
        try (FileWriter writer = new FileWriter("ttranslator/translation.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) throws IOException {
        System.out.println("loading config data");
        // Substitua MyDataClass pela classe dos dados que você deseja carregar.

        // Substitua "data.json" pelo caminho do arquivo de onde você deseja carregar os dados.
        Configs configs = new Configs();

        String path = "ttranslator/translation.json";
        if (Files.exists(Paths.get(path))) {
            try (FileReader reader = new FileReader(path)) {
                Gson gson = new Gson();
                configs = gson.fromJson(reader, Configs.class);

                // Agora myData contém seus dados carregados. Você pode usá-los conforme necessário.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ConfigService.getInstance().loadConfigs(configs);

        // Implemente a lógica para usar myData carregado aqui ou passe para outras partes do seu mod.
    }

}