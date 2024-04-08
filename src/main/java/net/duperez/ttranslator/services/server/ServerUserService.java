package net.duperez.ttranslator.services.server;

import net.duperez.ttranslator.entities.ServerUserEntity;

import java.util.ArrayList;
import java.util.List;

public class ServerUserService {

    public static ServerUserService instance;

    private List<ServerUserEntity> userEntityList;

    public static ServerUserService getInstance() {
        if(instance == null) {
            instance = new ServerUserService();
        }

        return instance;
    }

    private ServerUserService() {
        userEntityList = new ArrayList<>();
    }


    public void addUser(ServerUserEntity user) {
        userEntityList.add(user);
    }

    public void removeUser(String userName) {
        userEntityList.stream().filter(user -> user.getName().equals(userName)).findFirst().ifPresent(
                user -> userEntityList.remove(user)
        );
    }

    public ServerUserEntity findUser(String name) {
        return userEntityList.stream().filter(serverUserEntity -> serverUserEntity.getName().equals(name)).findFirst().orElse(null);
    }
}
