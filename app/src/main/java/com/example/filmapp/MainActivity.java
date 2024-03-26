package com.example.filmapp;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.GenreResponse;
import com.example.filmapp.api.response.MovieResponse;
import com.example.filmapp.api.response.VideoResponse;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    private static final String API_KEY = "02ddd233c99c814bad1a7d4af98e681b";
    private List<Movie> movies;
    private List<Genre> genres;
    private Call<MovieResponse> call1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create API interface instance
        Retrofit retrofit = RetrofitClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        Call<GenreResponse> call = apiInterface.getGenres(API_KEY, "en-US");
        call.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here
                    GenreResponse genreResponse = response.body();
                    genres = genreResponse.getGenres();
                    if (genreResponse != null && genreResponse.getGenres() != null) {
                        call1 = apiInterface.getTopRatedMovies(API_KEY, "en-US", 1);
                        // Make the API call
                        call1.enqueue(new Callback<MovieResponse>() {
                            @Override
                            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                                if (response.isSuccessful()) {
                                    // Handle successful response here
                                    MovieResponse movieResponse = response.body();
                                    movies = movieResponse.getMovies();
                                    if (movieResponse != null && movieResponse.getMovies() != null) {
                                        for (Movie movie : movieResponse.getMovies()) {
                                            // Process each movie here
                                            Log.d("Movie", "Title: " + movie.getTitle() + "\nDate: " +  movie.getReleaseDate());
//                                            Log.d("Genres", String.valueOf(movie.getGenreIdList().get(0)));
                                            List<Genre> movieGenres = new ArrayList<>();
                                            for (int id : movie.getGenreIdList()) {
                                                for (Genre genre : genres) {
                                                    if (genre.getId() == id) {
                                                        movieGenres.add(genre);
                                                    }
                                                }
                                            }
                                            movie.setGenreList(movieGenres);
                                            Log.d("geerere", movie.getGenreList().get(0).getName());
                                        }
//                        ImageView imageView = findViewById(R.id.imageView);
//                        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movieResponse.getMovies().get(0).getImagePath()).into(imageView);
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

                    }
                } else {
                    // Handle error response
                    Log.e("API Error", "Failed to fetch movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                // Handle failure
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });
//------------------------------------------------------------------------------------------------------------------





//
//                    // Make the API call
//                    Call<VideoResponse> call2 = apiInterface.getVideos(movies.get(1).getId(),API_KEY, "en-US");
//                    call2.enqueue(new Callback<VideoResponse>() {
//                        @Override
//                        public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
//                            if (response.isSuccessful()) {
//                                // Handle successful response here
//                                VideoResponse videoResponse = response.body();
//                                Video trailer = videoResponse.getTrailer();
//                                if (trailer != null) {
//                                    String trailerUrl = trailer.getUrl();
//                                    if (trailerUrl != null) {
//                                        Log.d("Trailer link", trailerUrl);
//
//
////                                        Code for embedding video
////                                        WebView webView = findViewById(R.id.webView);
////                                        String embedLink = "<iframe width=\"560\" height=\"315\" src=\"" + trailerUrl + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
////                                        webView.loadData(embedLink, "text/html", "utf-8");
////                                        webView.getSettings().setJavaScriptEnabled(true);
////                                        webView.setWebChromeClient(new WebChromeClient());
//
//
//                                    } else {
//                                        Log.e("Trailer link", "Trailer URL is null");
//                                    }
//                                } else {
//                                    Log.e("Trailer link", "Trailer object is null");
//                                }
//
//
//                            } else {
//                                // Handle error response
//                                Log.e("API Error", "Failed to fetch movies: " + response.message());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<VideoResponse> call, Throwable t) {
//                            // Handle failure
//                            Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
//                        }
//                    });


    }
}
