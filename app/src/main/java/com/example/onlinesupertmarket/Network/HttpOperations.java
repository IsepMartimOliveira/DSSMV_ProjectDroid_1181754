package com.example.onlinesupertmarket.Network;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpOperations {
    private static String readBody(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String sendRequest(String urlStr, String method, String data) {
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }

            httpConn = (HttpURLConnection) urlConn;
            httpConn.setRequestMethod(method);
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setDoOutput(true);

            if (data != null && !data.isEmpty() && (method.equals("POST") || method.equals("PUT"))) {
                try (OutputStream os = httpConn.getOutputStream()) {
                    byte[] input = data.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConn.getInputStream();
                return readBody(in);
            } else {
                // Handle error response here
                System.err.println("HTTP Request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return null;
    }

    public static String get(String urlStr) {
        return sendRequest(urlStr, "GET", null);
    }

    public static String post(String urlStr, String data) {
        return sendRequest(urlStr, "POST", data);
    }

    public static String delete(String urlStr) {
        return sendRequest(urlStr, "DELETE", null);
    }
}
