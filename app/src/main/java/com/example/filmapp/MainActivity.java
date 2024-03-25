package com.example.filmapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.GenreResponse;
import com.example.filmapp.api.response.MovieResponse;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;
import com.squareup.picasso.Picasso;

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

        // Make the API call
        Call<MovieResponse> call = apiInterface.getTopRatedMovies(API_KEY, "en-US", 1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null && movieResponse.getMovies() != null) {
                        for (Movie movie : movieResponse.getMovies()) {
                            // Process each movie here
                            Log.d("Movie", String.valueOf(movie.getRating()));
                        }
                        ImageView imageView = findViewById(R.id.imageView);
                        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movieResponse.getMovies().get(0).getImagePath()).into(imageView);
                    }
                } else {
                    // Handle error response
                    Log.e("API Error", "Failed to fetch movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Handle failure
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });
//
//        // Make the API call
//        Call<GenreResponse> call = apiInterface.getGenres(API_KEY, "en-US");
//        call.enqueue(new Callback<GenreResponse>() {
//            @Override
//            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
//                if (response.isSuccessful()) {
//                    // Handle successful response here
//                    GenreResponse genreResponse = response.body();
//                    if (genreResponse != null && genreResponse.getGenres() != null) {
//                        for (Genre genre : genreResponse.getGenres()) {
//                            // Process each movie here
//                            Log.d("Genre", genre.getName());
//                        }
//                    }
//                } else {
//                    // Handle error response
//                    Log.e("API Error", "Failed to fetch movies: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GenreResponse> call, Throwable t) {
//                // Handle failure
//                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
//            }
//        });

    }
}
