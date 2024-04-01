package com.example.filmapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.activities.ListsActivity;
import com.example.filmapp.activities.MovieDetailActivity;
import com.example.filmapp.application.repository.GenreRepository;
import com.example.filmapp.application.viewmodel.GenreViewModel;
import com.example.filmapp.application.viewmodel.MovieViewModel;
import com.example.filmapp.application.RecyclerViewInterface;
import com.example.filmapp.application.repository.Repository;
import com.example.filmapp.data.Database;
import com.example.filmapp.presentation.MyAdapter;
import com.example.filmapp.R;
import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.GenreResponse;
import com.example.filmapp.api.response.MoviesResponse;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private static final String API_KEY = "02ddd233c99c814bad1a7d4af98e681b";
    private List<Movie> movies;
    private List<Genre> genres;
    private Call<MoviesResponse> allMoviesCall;
    private Call<GenreResponse> allGenresCall;
    private MovieViewModel movieViewModel;
    // Create API interface instance
    private Retrofit retrofit = RetrofitClient.getClient();
    private ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    private RecyclerView recyclerView;
    private SearchView searchView;
    private GenreViewModel genreViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchbar);
        searchView.clearFocus();

        Repository repository = new Repository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieDao());
        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieViewModel.class);
        movieViewModel.init(repository); // Assuming you have an init() method in your MovieViewModel to initialize repository
        // Initialize the GenreViewModel
        GenreRepository genreRepository = new GenreRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).genreDao());
        genreViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(GenreViewModel.class);
        genreViewModel.init(genreRepository);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

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
                        movies = movieList;
                        MyAdapter adapter = new MyAdapter(getApplicationContext(), movies, MainActivity.this, genreViewModel, MainActivity.this);

                        recyclerView.setAdapter(adapter);

                    }
                });
            }

        });

        allGenresApiCall();


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

    private void filterList(String text) {
        List<Movie> filteredList = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(movie);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("aaa", "no yet set");
            ((MyAdapter) recyclerView.getAdapter()).setFilteredList(filteredList);
        }
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
        allMoviesCall = apiInterface.getTopRatedMovies(API_KEY, "en-US", 1);
        // Make the API call
        allMoviesCall.enqueue(new Callback<MoviesResponse>() {
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

                            movieViewModel.insertMovie(movie);

                        }


                        MyAdapter adapter = new MyAdapter(getApplicationContext(), movies, MainActivity.this, genreViewModel, MainActivity.this);
                        recyclerView.setAdapter(adapter);

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

    public void allGenresApiCall() {
        allGenresCall = apiInterface.getGenres(API_KEY, "en-US");

        // Make the API call
        allGenresCall.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {

                if (response.isSuccessful()) {
                    // Handle successful response here
                    GenreResponse genreResponse = response.body();

                    genres = genreResponse.getGenres();
                    if (genreResponse != null && genreResponse.getGenres() != null) {
                        for (Genre genre : genreResponse.getGenres()) {
                            // Process each movie here
                            Log.d("Genre", "Id: " + genre.getId() + "\nName: " +  genre.getName());

                            genreViewModel.insertGenre(genre);

                        }

//                        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), movies, MainActivity.this));

//                        ImageView imageView = findViewById(R.id.imageView);
//                        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movieResponse.getMovies().get(0).getImagePath()).into(imageView);
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
    }
    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("value", movie);
        intent.putExtras(bundle);
        Log.d("MainActivity", "clicked on movie: " + movie.getTitle());
        startActivity(intent);
    }
}

