package com.example.ttranslator.server.objects.player;

import com.example.ttranslator.server.objects.database.DataFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class PlayerOnFileManager extends DataFile<PlayerInfo> {
    private static PlayerOnFileManager instance;
    List<PlayerInfo> players;

    private PlayerOnFileManager() {
        players = loadDataList("translation.json");
    }

    public PlayerInfo getPlayerByUUID(String UUID) {
        return findPlayerByUUID(UUID);
    }

    private PlayerInfo findPlayerByUUID(String UUID) {
        for (PlayerInfo player : players) {
            if (player.getUUID().equals(UUID)) {
                return player;
            }
        }
        return null;
    }

    public void saveData() throws IOException {
        super.saveData("translation.json", players);
    }

    public static PlayerOnFileManager getInstance() {

        if (instance == null) {
            instance = new PlayerOnFileManager();
        }
        return instance;
    }

    public void addPlayer(PlayerInfo player) {
        players.add(player);
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public void removePlayer(PlayerInfo player) {
        players.remove(player);
    }
}