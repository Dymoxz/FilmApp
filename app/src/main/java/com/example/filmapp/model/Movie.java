package com.example.filmapp.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Movie implements Serializable {
    private int id;
    private String title;
    private float popularity;
    @SerializedName("vote_average")
    private float rating;
    @SerializedName("genre_ids")
    private List<Integer> genreIdList;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getPopularity() {
        return popularity;
    }

    public float getRating() {
        return 10*rating;
    }

    public List<Integer> getGenreIdList() {
        return genreIdList;
    }
}
