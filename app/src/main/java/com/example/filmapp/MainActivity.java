package com.example.filmapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.GenreRespone;
import com.example.filmapp.model.Genre;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    private static final String API_KEY = "02ddd233c99c814bad1a7d4af98e681b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create API interface instance
        Retrofit retrofit = RetrofitClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

//        // Make the API call
//        Call<MovieResponse> call = apiInterface.getTopRatedMovies(API_KEY, "en-US", 1);
//        call.enqueue(new Callback<MovieResponse>() {
//            @Override
//            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                if (response.isSuccessful()) {
//                    // Handle successful response here
//                    MovieResponse movieResponse = response.body();
//                    if (movieResponse != null && movieResponse.getMovies() != null) {
//                        for (Movie movie : movieResponse.getMovies()) {
//                            // Process each movie here
//                            Log.d("Movie", movie.getTitle());
//                        }
//                    }
//                } else {
//                    // Handle error response
//                    Log.e("API Error", "Failed to fetch movies: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieResponse> call, Throwable t) {
//                // Handle failure
//                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
//            }
//        });

        // Make the API call
        Call<GenreRespone> call = apiInterface.getGenres(API_KEY, "en-US");
        call.enqueue(new Callback<GenreRespone>() {
            @Override
            public void onResponse(Call<GenreRespone> call, Response<GenreRespone> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here
                    GenreRespone genreRespone = response.body();
                    if (genreRespone != null && genreRespone.getGenres() != null) {
                        for (Genre genre : genreRespone.getGenres()) {
                            // Process each movie here
                            Log.d("Genre", genre.getName());
                        }
                    }
                } else {
                    // Handle error response
                    Log.e("API Error", "Failed to fetch movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GenreRespone> call, Throwable t) {
                // Handle failure
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });

    }
}
