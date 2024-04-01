package com.example.filmapp.api;

import com.example.filmapp.api.response.GenreResponse;
import com.example.filmapp.api.response.MovieReviewResponse;
import com.example.filmapp.api.response.MoviesResponse;
import com.example.filmapp.api.response.VideoResponse;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.MovieReview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movieId}/videos")
    Call<VideoResponse> getVideos(
            @Path("movieId") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movieId}")
    Call<Movie> getMovieDetails(
            @Path("movieId") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewResponse> getReviews(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey

    );

}
