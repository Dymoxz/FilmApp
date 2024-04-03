package com.example.filmapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Video")
public class Video implements Serializable {
    @PrimaryKey
    private int movieId;
    @SerializedName("name")
    private String title;
    @SerializedName("key")
    private String videoKey;
    @SerializedName("official")
    private boolean isOfficial;
    private String type;

    public Video(int movieId, String title, String videoKey, Boolean isOfficial, String type) {
        this.movieId = movieId;
        this.title = title;
        this.videoKey = videoKey;
        this.isOfficial = isOfficial;
        this.type = type;
    }

    public String getUrl() {
        return "https://www.youtube.com/embed/" + videoKey;
    }

    public String getType() {
        return type;
    }

    public void setMovieId(int movieId) { this.movieId = movieId; }

    public int getMovieId() { return movieId; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

}
