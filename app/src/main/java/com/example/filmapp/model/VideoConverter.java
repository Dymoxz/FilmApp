package com.example.filmapp.model;

import androidx.room.TypeConverter;
import com.google.gson.Gson;

public class VideoConverter {
    @TypeConverter
    public static Video fromString(String value) {
        return new Gson().fromJson(value, Video.class);
    }

    @TypeConverter
    public static String fromVideo(Video video) {
        Gson gson = new Gson();
        return gson.toJson(video);
    }
}