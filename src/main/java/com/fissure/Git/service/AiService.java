package com.fissure.Git.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class AiService {
    public String generation(String prompt) {
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            String apiKey = System.getProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));

            if (apiKey == null) {
                throw new RuntimeException("GEMINI_API_KEY_NOT_SET");
            }
            String url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent?key=" + apiKey;
            String escapedPrompt = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");

            String body = "{ \"contents\": [{ \"role\": \"user\", \"parts\": [{\"text\": \""
                    + escapedPrompt + "\"}]}]}";

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes());
            }

            InputStream stream = conn.getResponseCode() >= 400
                    ? conn.getErrorStream()
                    : conn.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String response = br.lines().reduce("", (a, b) -> a + b);

            System.out.println("PROMPT:\n" + prompt);
            System.out.println("RESPONSE:\n" + response);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}
