package com.example.onlinesupertmarket.Mapper;

import com.google.gson.Gson;

public class ConvertDTOtoJSON {

    public static String convertToJson(Object dtoObject) {
        Gson gson = new Gson();
        return gson.toJson(dtoObject);
    }

    public static <T> T convertFromJson(String json, Class<T> dtoClass) {
        Gson gson = new Gson();
        return gson.fromJson(json, dtoClass);
    }
}
