package net.duperez.ttranslator.server.services;

import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.server.entities.ServerSideClientConfigs;
import net.duperez.ttranslator.common.services.interfaces.ClientServiceInterface;
import net.duperez.ttranslator.common.services.interfaces.SingletonInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerSideClientService extends SingletonInterface implements ClientServiceInterface {

    public static ServerSideClientService instance;

    private List<ServerSideClientConfigs> userEntityList = new ArrayList<>();


    public static ServerSideClientService getInstance() {
        if (instance == null) {
            instance = new ServerSideClientService();
        }

        return instance;
    }

    public void removeClient(String clientName) {
        userEntityList.stream().filter(user -> user.getName().equals(clientName)).findFirst().ifPresent(
                user -> userEntityList.remove(user)
        );
    }

    public ServerSideClientConfigs findUser(String name) {
        return userEntityList.stream().filter(serverUserEntity -> serverUserEntity.getName().equals(name)).findFirst().orElse(null);
    }


    public List<Language> findAllCurrentLanguages() {
        //return a list of all the languages on "readLanguages" field
        Set<Language> allLanguages = new HashSet<>();
        userEntityList.forEach(user -> allLanguages.addAll(user.getReadLanguage()));
        return allLanguages.stream().distinct().collect(Collectors.toList());
    }

    public void addUser(ServerSideClientConfigs user) {
        userEntityList.add(user);
    }
}
