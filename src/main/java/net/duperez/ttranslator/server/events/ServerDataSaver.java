package net.duperez.ttranslator.server.events;

import com.google.gson.Gson;
import net.duperez.ttranslator.server.entities.ServerTranslationConfigsEntity;
import net.duperez.ttranslator.server.services.ServerSideConfigsService;
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
    private static final String CONFIG_DIRECTORY_PATH = "ttranslator";
    private static final String CONFIG_FILE_PATH = "translation.json";
    private static final Gson GSON = new Gson();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ServerSideConfigsService.getInstance().setConfigs(loadConfigsFromJsonIfExists(CONFIG_DIRECTORY_PATH + "/" + CONFIG_FILE_PATH));
    }

    private static ServerTranslationConfigsEntity loadConfigsFromJsonIfExists(String path) {
        if (Files.exists(Paths.get(path))) {
            try (FileReader reader = new FileReader(path)) {
                return GSON.fromJson(reader, ServerTranslationConfigsEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {

        initializeNullTranslationKey(ServerSideConfigsService.getInstance().getConfigs());

        createDirectory(CONFIG_DIRECTORY_PATH);

        try (FileWriter writer = new FileWriter(CONFIG_DIRECTORY_PATH + "/" + CONFIG_FILE_PATH)) {
            writer.write(GSON.toJson(ServerSideConfigsService.getInstance().getConfigs()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeNullTranslationKey(ServerTranslationConfigsEntity configs) {
        if (configs.getTranslationKey() == null) {
            configs.setTranslationKey("");
        }
    }

    private static void createDirectory(String directoryPath) {
        try {
            Files.createDirectories(Paths.get(directoryPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}