package com.example.onlinesupertmarket.Network;

import okhttp3.*;

public class HttpClient {
    private static final OkHttpClient client = new OkHttpClient();

    public static void getRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void postRequest(String url, String jsonBody, Callback callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void deleteRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(callback);
    }
}