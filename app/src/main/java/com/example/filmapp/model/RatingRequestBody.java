package com.example.filmapp.model;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


public class RatingRequestBody {
    @SerializedName("value")

    private float value;

    public RatingRequestBody(float value) {
        this.value = value;
    }

}
