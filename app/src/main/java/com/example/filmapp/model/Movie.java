package com.example.filmapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Movie implements Serializable {
    private int id;
    private String title;
    @SerializedName("poster_path")
    private String imagePath;
    @SerializedName("genre_ids")
    private List<Integer> genreIdList;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getGenreIdList() {
        return genreIdList;
    }

    public String getImagePath() {
        return imagePath;
    }
}
