package com.example.ttranslator.server.objects.database;

import com.example.ttranslator.server.objects.player.PlayerInfo;
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

public class DataFile<T> {
    private static final String PATH = "config\\ttranslator";
    private static final Gson gson = new Gson();


    public List<T> loadDataList(String fileName) {
        List<T> dataList = new ArrayList<>();
        try (FileReader reader = new FileReader(PATH + "\\" + fileName)) {
            Type listType = new TypeToken<List<T>>(){}.getType();
            dataList = gson.fromJson(reader, listType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void saveData(String fileName, List<T> content) throws IOException {
        Files.createDirectories(Paths.get(PATH));
        String json = gson.toJson(content);
        try (FileWriter writer = new FileWriter(PATH + "\\" + fileName)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
