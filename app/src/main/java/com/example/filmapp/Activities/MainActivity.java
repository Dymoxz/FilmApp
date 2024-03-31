package com.example.filmapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.Application.MovieViewModel;
import com.example.filmapp.Application.RecyclerViewInterface;
import com.example.filmapp.Application.Repository;
import com.example.filmapp.Data.Database;
import com.example.filmapp.MyAdapter;
import com.example.filmapp.R;
import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.MoviesResponse;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {


    private static final String API_KEY = "02ddd233c99c814bad1a7d4af98e681b";
    private List<Movie> movies;
    private List<Genre> genres;
    private Call<MoviesResponse> call1;
    private MovieViewModel movieViewModel;
    // Create API interface instance
    private Retrofit retrofit = RetrofitClient.getClient();
    private ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        Repository repository = new Repository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieDao());
        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieViewModel.class);
        movieViewModel.init(repository); // Assuming you have an init() method in your MovieViewModel to initialize repository

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);

        }



        movieViewModel.moviesIsEmpty().observe(this, isEmpty -> {
            if (isEmpty) {
                allMoviesApiCall();
            } else {
                Log.d("MainActivity", "Database not empty");

                // Observe the LiveData object
                movieViewModel.getListMovies().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movieList) {
                        List<Movie> movies = movieList;
                        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), movies, MainActivity.this));
                    }
                });



            }


        });





        //--------------------------------------------------------------------------------------------





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
        // Make the API call
        Call<Movie> call2 = apiInterface.getMovieDetails(240, API_KEY, "en-US");
        call2.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here
                    Movie movie = response.body();
                    if (movie != null) {
                        // Movie data is available
                        Log.d("Movie", "Title: " + movie.getDuration());
                        Log.d("Movie", "Tagline: " + movie.getTagline());
                        Log.d("Movie", "Rating: " + movie.getRating());

                    } else {
                        // MovieDetailResponse is null
                        Log.e("API Error", "Movie object is null");
                    }
                } else {
                    // Handle error response
                    Log.e("API Error", "Failed to fetch movies. Error code: " + response.code());
                    try {
                        Log.e("API Error", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("API Error", "Failed to read error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Handle failure
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });


    }
    public void changeActivityToLists(View view){
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void allMoviesApiCall() {
        call1 = apiInterface.getTopRatedMovies(API_KEY, "en-US", 1);
        // Make the API call
        call1.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here
                    MoviesResponse moviesResponse = response.body();
                    movies = moviesResponse.getMovies();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        for (Movie movie : moviesResponse.getMovies()) {
                            // Process each movie here
                            Log.d("Movie", "Title: " + movie.getTitle() + "\nDate: " +  movie.getReleaseDate());
//                                            Log.d("Genres", String.valueOf(movie.getGenreIdList().get(0)));


                            movieViewModel.insertMovie(movie);


                        }

                        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), movies, MainActivity.this));




//                        ImageView imageView = findViewById(R.id.imageView);
//                        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movieResponse.getMovies().get(0).getImagePath()).into(imageView);
                    }

                } else {
                    // Handle error response
                    Log.e("API Error", "Failed to fetch movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Handle failure
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", (Serializable) movie);
        startActivity(intent);
    }
}

