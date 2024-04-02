package com.example.filmapp.model;

public class RatingRequestBody {
    private float value;

    public RatingRequestBody(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
