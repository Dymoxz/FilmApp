package com.example.filmapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Video implements Serializable {
    @SerializedName("name")
    private String title;
    private String key;
    @SerializedName("official")
    private boolean isOfficial;
    private String type;

    public Video(String title, String key, Boolean isOfficial, String type) {
        this.title = title;
        this.key = key;
        this.isOfficial = isOfficial;
        this.type = type;
    }

    public String getUrl() {
        return "https://www.youtube.com/watch?v=" + key;
    }

    public Boolean getOfficial() {
        return isOfficial;
    }

    public String getType() {
        return type;
    }
}
