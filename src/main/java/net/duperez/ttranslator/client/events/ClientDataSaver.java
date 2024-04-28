package net.duperez.ttranslator.client.events;

import com.google.gson.Gson;
import net.duperez.ttranslator.client.entities.ClientSideClientConfigs;
import net.duperez.ttranslator.client.services.ClientSideClientLanguageService;
import net.duperez.ttranslator.common.messages.UserPackage;
import net.duperez.ttranslator.common.network.ModNetworking;
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

    private static final String PATH = "ttranslator/translation.json";
    private static final Gson gson = new Gson();

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggedInEvent event) {
        if (Files.exists(Paths.get(PATH))) {
            System.out.println("User logged in.");
            loadUserData();
        }
    }

    public static void loadUserData() {
        try (FileReader reader = new FileReader(PATH)) {

            ClientSideClientConfigs user = gson.fromJson(reader, ClientSideClientConfigs.class);
            handleUserLoggedIn(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleUserLoggedIn(ClientSideClientConfigs user) {
        ClientSideClientLanguageService.getInstance().setClientEntity(user);
        UserPackage userPackage = new UserPackage(gson.toJson(user));
        ModNetworking.CHANNEL.sendToServer(userPackage);
    }

    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggedOutEvent event) throws IOException {
        if (ClientSideClientLanguageService.getInstance().getClientEntity() != null) {
            saveUserData(ClientSideClientLanguageService.getInstance().getClientEntity());
        }
    }

    private static void saveUserData(ClientSideClientConfigs user) throws IOException {
        Files.createDirectories(Paths.get("ttranslator"));
        String json = gson.toJson(user);
        try (FileWriter writer = new FileWriter(PATH)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}