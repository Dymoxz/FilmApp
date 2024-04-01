package com.example.filmapp.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MovieReviewListConverter {
    @TypeConverter
    public static List<MovieReview> fromString(String value) {
        Type listType = new TypeToken<List<MovieReview>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<MovieReview> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
