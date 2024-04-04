package com.example.filmapp.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.application.repository.GenreRepository;
import com.example.filmapp.application.repository.MovieListRepository;
import com.example.filmapp.application.viewmodel.GenreViewModel;
import com.example.filmapp.application.viewmodel.MovieListViewModel;
import com.example.filmapp.application.viewmodel.MovieViewModel;
import com.example.filmapp.application.RecyclerViewInterface;
import com.example.filmapp.application.repository.Repository;
import com.example.filmapp.data.Database;
import com.example.filmapp.model.IntegerListConverter;
import com.example.filmapp.model.MovieList;
import com.example.filmapp.presentation.MyAdapter;
import com.example.filmapp.R;
import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.GenreResponse;
import com.example.filmapp.api.response.MoviesResponse;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {

    private static final String API_KEY = "02ddd233c99c814bad1a7d4af98e681b";
    private List<Movie> movies;
    private MovieViewModel movieViewModel;
    private final Retrofit retrofit = RetrofitClient.getClient();
    private final ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    private RecyclerView recyclerView;
    private GenreViewModel genreViewModel;
    private MovieListViewModel movieListViewModel;
    private RelativeLayout relativeLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        SearchView searchView = findViewById(R.id.searchbar);
        relativeLayout = findViewById(R.id.main);
        toolbar = findViewById(R.id.toolbar);

        searchView.clearFocus();

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

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_home_24);
        }

        initViewModels();
        createStandardLists();


        movieViewModel.moviesIsEmpty().observe(this, isEmpty -> {
            if (isEmpty) {
                int pageNums = 3;
                for (int i = 0; i <= pageNums; i++){
                    allMoviesApiCall(i);
                }
                MyAdapter adapter = new MyAdapter(getApplicationContext(), movies, MainActivity.this, genreViewModel, MainActivity.this, getClass().getSimpleName());
                recyclerView.setAdapter(adapter);
            } else {
                Log.d("MainActivity", "Database not empty");

                // Observe the LiveData object
                movieViewModel.getListMovies().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movieList) {
                        movies = movieList;
                        MyAdapter adapter = new MyAdapter(getApplicationContext(), movies, MainActivity.this, genreViewModel, MainActivity.this, getClass().getSimpleName());

                        recyclerView.setAdapter(adapter);

                    }
                });
            }

        });

        allGenresApiCall();



        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void filterList(String text) {
        List<Movie> filteredList = new ArrayList<>();
        if (movies != null) {
            for (Movie movie : movies) {
                if (movie.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(movie);
                }
            }
        }


        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(null);
        } else {
            Log.d("aaa", "no yet set");
            if ((MyAdapter) recyclerView.getAdapter() != null) {
                ((MyAdapter) recyclerView.getAdapter()).setFilteredList(filteredList);
            } else {
                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredList, MainActivity.this, genreViewModel, MainActivity.this, getClass().getSimpleName());
                recyclerView.setAdapter(adapter);
            }

        }
    }

    private void filterList2(String text) {
        List<Movie> filteredList = new ArrayList<>();
        if (movies != null) {
            for (Movie movie : movies) {
                List<String> genreNameList = new ArrayList<>();
                AtomicInteger genreNamesFetched = new AtomicInteger(0); // Counter to track fetched genre names
                for (int id : movie.getGenreIdList()) {
                    genreViewModel.getGenreName(id).observe(this, genreName -> {
                        if (genreName != null) {
                            genreNameList.add(genreName.toLowerCase());
                            int fetchedCount = genreNamesFetched.incrementAndGet(); // Increment counter
                            // Check if all genre names have been fetched
                            if (fetchedCount == movie.getGenreIdList().size()) {
                                // Now genreNameList is fully populated, you can proceed with further operations
                                if (genreNameList.contains(text.toLowerCase())) {
                                    filteredList.add(movie);
                                }
                                if (filteredList.isEmpty()) {
                                    recyclerView.setAdapter(null);
                                } else {
                                    if ((MyAdapter) recyclerView.getAdapter() != null) {
                                        ((MyAdapter) recyclerView.getAdapter()).setFilteredList(filteredList);
                                    } else {
                                        MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredList, MainActivity.this, genreViewModel, MainActivity.this, getClass().getSimpleName());
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            }
                        }
                    });
                }
            }
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

    public void allMoviesApiCall(int page) {
        Call<MoviesResponse> allMoviesCall = apiInterface.getTopRatedMovies(API_KEY, "en-US", page);
        // Make the API call
        allMoviesCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        // Ensure that the list is not null before adding elements to it
                        if (movies != null) {
                            // Add movies to the existing list
                            movies.addAll(moviesResponse.getMovies());
                        } else {
                            // If movies list is null, initialize it and then add movies
                            movies = new ArrayList<>();
                            movies.addAll(moviesResponse.getMovies());
                        }
                        if (moviesResponse.getMovies() != null) {
                            for (Movie movie : moviesResponse.getMovies()) {
                                // Process each movie here
                                Log.d("Movie", "Title: " + movie.getTitle() + "\nDate: " + movie.getReleaseDate());
                                movieViewModel.insertMovie(movie);
                            }
                        }
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
        Call<GenreResponse> allGenresCall = apiInterface.getGenres(API_KEY, "en-US");

        // Make the API call
        allGenresCall.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {

                if (response.isSuccessful()) {
                    // Handle successful response here
                    GenreResponse genreResponse = response.body();

                    if (genreResponse != null && genreResponse.getGenres() != null) {
                        for (Genre genre : genreResponse.getGenres()) {
                            // Process each movie here
                            Log.d("Genre", "Id: " + genre.getId() + "\nName: " +  genre.getName());
                            genreViewModel.insertGenre(genre);
                        }
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

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Movie movie = movies.get(position);
            if (direction == ItemTouchHelper.LEFT){
                //logic for swiping right, add to favorites
                addMovieIdToList("Favorites", movie.getId());
                recyclerView.getAdapter().notifyItemChanged(position);
                Log.v("MainActivity", "Added " + movie.getTitle() + " to favorites");
                Snackbar snackbar = Snackbar.make(relativeLayout, "Added " + movie.getTitle() + " to favorites", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if (direction == ItemTouchHelper.RIGHT){
                //logic for swiping right, add to watchlater
                addMovieIdToList("Watch later", movie.getId());
                recyclerView.getAdapter().notifyItemChanged(position);
                Log.v("MainActivity", "Added " + movie.getTitle() + " to watch later");
                Snackbar snackbar = Snackbar.make(relativeLayout, "Added " + movie.getTitle() + " to watch later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Add to favorites")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.baseline_favorite_24)
                    .setActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.green))

                    .addSwipeRightLabel("Add to watch later")
                    .setSwipeRightLabelColor(getResources().getColor(R.color.white))
                    .addSwipeRightActionIcon(R.drawable.baseline_watch_later_24)
                    .setActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.blue))

                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void initViewModels(){
        Repository repository = new Repository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieDao());
        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieViewModel.class);
        movieViewModel.init(repository); // Assuming you have an init() method in your MovieViewModel to initialize repository
        // Initialize the GenreViewModel
        GenreRepository genreRepository = new GenreRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).genreDao());
        genreViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(GenreViewModel.class);
        genreViewModel.init(genreRepository);
        // Initialize the MovieListViewModel
        MovieListRepository movieListRepository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
        movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
        movieListViewModel.init(movieListRepository);

    }

    public void addMovieIdToList(String listName, int movieId) {
        LiveData<String> movieIdListLiveData = movieListViewModel.getMovieIdList(listName);
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String movieIdList) {
                if (movieIdList != null) {
                    List<Integer> movieIdListInt = IntegerListConverter.fromString(movieIdList);
                    if (movieIdListInt == null) {
                        movieIdListInt = new ArrayList<>();
                    }
                    if (!movieIdListInt.contains(movieId)) {
                        movieIdListInt.add(movieId);
                    } else {
                        Log.v("MainActivity", "Already in list: " + movieId);
                    }
                    String updatedMovieIdList = IntegerListConverter.fromList(movieIdListInt);
                    movieListViewModel.updateMovieIdList(updatedMovieIdList, listName);
                }
                // Remove the observer after it's done
                movieIdListLiveData.removeObserver(this);
            }
        };
        movieIdListLiveData.observe(this, observer);
    }

    public void createStandardLists(){
        movieListViewModel.getMovieLists().observe(this, movieListsLiveData -> {
            if (movieListsLiveData != null && movieListsLiveData.isEmpty()) {
                // Add default lists if the database is empty
                MovieList favorites = new MovieList("Favorites");
                MovieList watchLater = new MovieList("Watch later");
                movieListsLiveData.add(favorites);
                movieListsLiveData.add(watchLater);
                movieListViewModel.insertMovieList(favorites);
                movieListViewModel.insertMovieList(watchLater);
                Log.v("ListActivity", "Added Favorites and Watch later lists");
            }
        });

    }


    private String currentFilter = null;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dramaFilter || id == R.id.crimeFilter || id == R.id.animationFilter || id == R.id.fantasyFilter) {
            toggleFilter(item.getTitle().toString());
            updateFilterButtonVisual(item);
            if (item.isChecked()) {
                Menu menu = toolbar.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem menuItem = menu.getItem(i);
                    if (menuItem.getItemId() != id) {
                        menuItem.setChecked(false);
                    }
                }
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void toggleFilter(String filter) {
        if (filter.equals(currentFilter)) {
            currentFilter = null;
            loadAllMovies();
            updateFilterButtonVisual(null);
        } else {
            currentFilter = filter;
            filterList2(filter);
        }
    }

    private void loadAllMovies() {
        if (movies != null) {
            MyAdapter adapter = new MyAdapter(getApplicationContext(), movies, MainActivity.this, genreViewModel, MainActivity.this, getClass().getSimpleName());
            recyclerView.setAdapter(adapter);
        }
    }

    private void updateFilterButtonVisual(@Nullable MenuItem selectedItem) {
        Menu menu = toolbar.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setChecked(false);
        }

        if (selectedItem != null) {
            selectedItem.setChecked(true);
        }
    }




}

