package com.example.filmapp.api;

import com.example.filmapp.api.response.CastResponse;
import com.example.filmapp.api.response.GenreResponse;
import com.example.filmapp.api.response.GuestSessionResponse;
import com.example.filmapp.api.response.MovieDetailResponse;
import com.example.filmapp.api.response.MovieReviewResponse;
import com.example.filmapp.api.response.MoviesResponse;
import com.example.filmapp.api.response.VideoResponse;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.MovieReview;
import com.example.filmapp.model.RatingRequestBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    Call<MovieDetailResponse> getMovieDetails(
            @Path("movieId") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movieId}/reviews")
    Call<MovieReviewResponse> getReviews(
            @Path("movieId") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/credits")
    Call<CastResponse> getCredits(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @POST("movie/{movie_id}/rating")
    Call<Void> postMovieRating(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("guest_session_id") String guestSessionId,
            @Body RatingRequestBody ratingRequestBody,
            @Header("Content-Type") String contentType

            );

    @GET("authentication/guest_session/new")
    Call<GuestSessionResponse> getGuestSession(
    @Query("api_key") String apiKey
    );
}
