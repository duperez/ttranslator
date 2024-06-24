package com.example.ttranslator.server.objects.player;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

public class PlayerOnMemoryManager {
    private static volatile PlayerOnMemoryManager instance;
    private final List<PlayerInfo> players;

    private PlayerOnMemoryManager() {
        this.players = new ArrayList<>();
    }

    public static PlayerOnMemoryManager getInstance() {
        if (instance == null) {
            synchronized (PlayerOnMemoryManager.class) {
                if (instance == null)
                    instance = new PlayerOnMemoryManager();
            }
        }
        return instance;
    }

    public void loadPlayer(String UUID) {
        PlayerInfo playerInfo = PlayerOnFileManager.getInstance().getPlayerByUUID(UUID);
        if(playerInfo != null) {
            players.add(playerInfo);
        } else {
            playerInfo = new PlayerInfo(UUID);
            playerInfo.setTranslating(false);
            playerInfo.setShowOriginalMessages(true);
            players.add(playerInfo);
            PlayerOnFileManager.getInstance().addPlayer(playerInfo);
        }
    }

    public void addPlayer(PlayerInfo player) {
        this.players.add(player);
    }

    public void updatePlayer(PlayerInfo player) {
        PlayerOnFileManager.getInstance().getPlayers().remove(player);
        PlayerOnFileManager.getInstance().addPlayer(player);
    }

    public List<PlayerInfo> getPlayers() {
        return this.players;
    }

    public Optional<PlayerInfo> getPlayerByUUID(String UUID) {
        return this.players.stream()
                .filter(player -> player.getUUID().equals(UUID))
                .findFirst();
    }

    public void removePlayer(PlayerInfo player) {
        this.players.remove(player);
    }
}