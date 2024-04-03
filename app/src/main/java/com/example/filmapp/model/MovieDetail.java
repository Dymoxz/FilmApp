package com.example.filmapp.model;

import com.google.gson.annotations.SerializedName;

public class MovieDetail {
    private String tagline;

    @SerializedName("runtime")
    private int duration;

    public String getTagline() {
        return tagline;
    }

    public int getDuration() {
        return duration;
    }
}
