package com.example.filmapp.api.response;

import com.example.filmapp.model.MovieReview;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MovieReviewResponse {
    @SerializedName("results")
    private List<MovieReview> reviews;

    public List<MovieReview> getReviews() {
        return reviews;
    }
}
