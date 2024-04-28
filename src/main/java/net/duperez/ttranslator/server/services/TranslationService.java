package net.duperez.ttranslator.server.services;

import net.duperez.ttranslator.objects.common.Language;
import net.duperez.ttranslator.objects.common.ToTranslateMessageEntity;
import net.duperez.ttranslator.objects.common.TranslatedMessageEntity;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationService {


    public static List<TranslatedMessageEntity> translateMessageMultiTread(ToTranslateMessageEntity message, List<Language> languages) throws ExecutionException, InterruptedException {
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        List<TranslatedMessageEntity> translatedMessages = new ArrayList<>();
        for (Language language : languages) {
            futures.add(executor.submit(() -> {
                try {
                    translatedMessages.add(translateMessage(message.getMessage(), message.getLanguage().getIsoName(), language.getIsoName(), message.getSender()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        for (Future<?> future : futures) {
            future.get();
        }

        return translatedMessages;
    }

    public static TranslatedMessageEntity translateMessage(String message, String originalLanguage, String newLanguage, String sender) throws IOException {
        return newLanguage.equals(originalLanguage) ?
                new TranslatedMessageEntity(message,
                        new Language(originalLanguage, originalLanguage),
                        new Language(newLanguage, newLanguage),
                        sender) :
                new TranslatedMessageEntity(
                        getTranslation(message, originalLanguage, newLanguage, ServerSideConfigsService.getInstance().getConfigs().getTranslationKey()),
                        new Language(originalLanguage, originalLanguage),
                        new Language(newLanguage, newLanguage),
                        sender);
    }

    public static String getTranslation(String originalText, String l1, String l2, String key) throws IOException {
        String requestUrl = constructRequestURL(originalText, l1, l2, key);
        HttpURLConnection connection = setUpConnection(requestUrl);
        String translationResult = processHttpResponse(connection);
        connection.disconnect();
        return StringEscapeUtils.unescapeHtml4(translationResult);
    }

    private static String constructRequestURL(String originalText, String l1, String l2, String key) throws UnsupportedEncodingException, UnsupportedEncodingException {
        String encodedText = URLEncoder.encode(originalText, StandardCharsets.UTF_8);
        return "https://translation.googleapis.com/language/translate/v2"
                + "?key=" + key
                + "&q=" + encodedText
                + "&source=" + l1
                + "&target=" + l2;
    }

    private static HttpURLConnection setUpConnection(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection;
    }

    private static String processHttpResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getValue(readHttpResponse(connection));
        } else {
            System.out.println("Erro na requisição: " + responseCode);
            return "";
        }
    }

    private static String readHttpResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
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
