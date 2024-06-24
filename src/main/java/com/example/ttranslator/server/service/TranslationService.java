package com.example.ttranslator.server.service;

import com.example.ttranslator.server.objects.language.TranslatedMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.logging.LogUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;


public class TranslationService {

    public static String KEY = "<your_key_here";
    private static final String API_URL = "https://texttospeech.googleapis.com/v1/text:synthesize?key=" + KEY;

    private static final Logger LOGGER = LogUtils.getLogger();

    public static List<TranslatedMessage> translateMessageMultiTread(String message, String originalLanguage, List<String> languages) throws ExecutionException, InterruptedException {
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        List<TranslatedMessage> translatedMessages = new ArrayList<>();
        for (String language : languages) {
            futures.add(executor.submit(() -> {
                try {
                    translatedMessages.add(new TranslatedMessage(getTranslation(message, originalLanguage, language, KEY), originalLanguage, language));
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

    public static String detectLanguage(String text) throws IOException {
        LOGGER.info("INSIDE DETECT LANGUAGE METHOD");
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
        String requestUrl = "https://translation.googleapis.com/language/translate/v2/detect?key=" + KEY + "&q=" + encodedText;
        LOGGER.info("URL CREATED: " + requestUrl);
        HttpURLConnection connection = setUpConnection(requestUrl);
        String detectionResult = processHttpResponse(connection);
        LOGGER.info("DETECTION RESULT " + detectionResult);
        connection.disconnect();
        return detectionResult;
    }

    public static String getDetectedLanguage(String originalText) {
        LOGGER.info(originalText);
        String regex = "\"language\":\\s*\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        LOGGER.info(originalText);
        Matcher matcher = pattern.matcher(originalText);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static String getTranslation(String originalText, String l1, String l2, String key) throws IOException {
        String requestUrl = constructTranslationRequestURL(originalText, l1, l2, key);
        HttpURLConnection connection = setUpConnection(requestUrl);
        String translationResult = processTranslationHttpResponse(connection);
        connection.disconnect();
        return StringEscapeUtils.unescapeHtml4(translationResult);
    }

    private static String constructTranslationRequestURL(String originalText, String l1, String l2, String key) {
        String encodedText = URLEncoder.encode(originalText, StandardCharsets.UTF_8);
        return "https://translation.googleapis.com/language/translate/v2"
                + "?key=" + key
                + "&q=" + encodedText
                + "&source=" + l1
                + "&target=" + l2;
    }

    private static HttpURLConnection setUpConnection(String requestUrl) throws IOException {
        LOGGER.info("SETTING UP CONNECTION " + requestUrl);
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection;
    }

    private static String processTranslationHttpResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getValue(readHttpResponse(connection));
        } else {
            System.out.println("Erro na requisição: " + responseCode);
            return "";
        }
    }

    private static String processHttpResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getDetectedLanguage(readHttpResponse(connection));
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


    //public static void main(String[] args) {
    //    speak("Hello world");
    //    System.out.println("HI");
    //}

    public static byte[] generateTTS(String text) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = createRequestBody(text);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonRequest.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    JsonNode jsonNode = objectMapper.readValue(response.toString(), JsonNode.class);
                    String audioContent = jsonNode.get("audioContent").asText();

                    return Base64.getDecoder().decode(audioContent);
                }
            } else {
                System.err.println("Request failed: " + responseCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static void speak(String text) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = createRequestBody(text);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonRequest.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    JsonNode jsonNode = objectMapper.readValue(response.toString(), JsonNode.class);
                    String audioContent = jsonNode.get("audioContent").asText();
                    byte[] audioBytes = Base64.getDecoder().decode(audioContent);

                    playSound(audioBytes);
                }
            } else {
                System.err.println("Request failed: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createRequestBody(String text) {
        return "{"
                + "\"input\": {\"text\": \"" + text + "\"},"
                + "\"voice\": {\"languageCode\": \"pt-BR\", \"ssmlGender\": \"NEUTRAL\"},"
                + "\"audioConfig\": {\"audioEncoding\": \"LINEAR16\"}" // Use "LINEAR16" para obter dados brutos de áudio PCM
                + "}";
    }

    private static void playSound(byte[] audioBytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bais);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();

            // Esperar o áudio terminar de tocar
            while (!clip.isRunning())
                Thread.sleep(10);
            while (clip.isRunning())
                Thread.sleep(10);

            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
