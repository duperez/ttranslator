package net.duperez.ttranslator.services.server;

import net.duperez.ttranslator.entities.Language;
import net.duperez.ttranslator.entities.Message;
import net.duperez.ttranslator.services.common.ConfigService;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationService {


    public static List<Message> translateMessageMultiTread(Message message, List<Language> lanauges) throws ExecutionException, InterruptedException {
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Message> translatedMessages = new ArrayList<>();
        for(Language language : lanauges) {
            futures.add(executor.submit(() -> {
                try {
                    translatedMessages.add(translateMessage(message.getMessage(), message.getOriginalLanguage().getSmName(), language.getSmName(), message.getSender()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        for (Future<?> future : futures) {
            future.get(); // Wait for the task to finish
        }

        return translatedMessages;
    }


    public static Message translateMessage(String message, String originalLanguage, String newLanguage, String sender) throws IOException {
        System.out.println("key" + ConfigService.getInstance().getConfigs().getTranslationKey());
        if(newLanguage.equals(originalLanguage)) {
            return new Message(message, new Language(newLanguage, newLanguage), new Language(originalLanguage, originalLanguage), sender);

        }
        return new Message(getTranslation(message, originalLanguage, newLanguage, ConfigService.getInstance().getConfigs().getTranslationKey()), new Language(newLanguage, newLanguage), new Language(originalLanguage, originalLanguage), sender);
    }

    public static String getTranslation(String textoOriginal, String l1, String l2, String key) throws IOException {
        String result = "";
        System.out.println("Entrada: " + textoOriginal);
        System.out.println("L1: " + l1);
        System.out.println("L2: " + l2);
        String encodedText = URLEncoder.encode(textoOriginal, "UTF-8");
        String requestUrl = "https://translation.googleapis.com/language/translate/v2"
                + "?key=" + key
                + "&q=" + encodedText
                + "&source=" + l1
                + "&target=" + l2;

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();
            System.out.println("Saida: " + jsonResponse);
            // Extrai a tradução do JSON de resposta
            result = getValue(jsonResponse);

            // Imprime o texto traduzido
            System.out.println("Texto original: " + textoOriginal);
            System.out.println("Texto traduzido: " + result);
        } else {
            System.out.println("Erro na requisição: " + responseCode);
        }
        connection.disconnect();
        return StringEscapeUtils.unescapeHtml4(result);
    }

    public static String getValue(String originalText) {
        // Regular expression pattern
        String regex = "\"translatedText\":\\s*\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(originalText);

        // Find the matching pattern
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}
