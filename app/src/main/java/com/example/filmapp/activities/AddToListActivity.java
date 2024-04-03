package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;
import com.example.filmapp.application.ListRecyclerViewInterface;
import com.example.filmapp.application.repository.MovieListRepository;
import com.example.filmapp.application.viewmodel.MovieListViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.model.IntegerListConverter;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.MovieList;
import com.example.filmapp.presentation.AddToListAdapter;
import com.example.filmapp.presentation.ListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AddToListActivity extends AppCompatActivity implements ListRecyclerViewInterface {
    private MovieListViewModel movieListViewModel;
    private List<MovieList> movieLists;
    private RadioButton radioButton;
    private Button button;
    private Movie movie;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_list);


        View inflatedView = getLayoutInflater().inflate(R.layout.add_to_list_item_row, null);
        radioButton = inflatedView.findViewById(R.id.button);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("MOVIE");
        Log.v("AddToListactivity", "movie id: " + movie.getId());
        MovieListRepository repository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
        movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
        movieListViewModel.init(repository);
        movieListViewModel.getMovieLists().observe(this, movieListsLiveData -> {
            if (movieListsLiveData != null) {
                movieLists = movieListsLiveData; // Assuming movieLists is a List<MovieList> variable
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new AddToListAdapter(getApplicationContext(), movieLists, AddToListActivity.this));
            }
        // movieIdList = ["11,34", "21,21"]
        // movie

        });

    }

    @Override
    public void onItemClick(MovieList movieList) {
        radioButton.toggle();
    }

    public void addMovieToListsButton(View view){
        AddToListAdapter adapter = new AddToListAdapter(this, movieLists, AddToListActivity.this);


        List<String> selectedListNames = adapter.getSelectedListNames();

        for (String listName : selectedListNames) {
            Log.d("Selected List Name", listName);
            addMovieIdToList(listName, movie.getId());
        }
        Intent gottenIntent = getIntent();
        String previousPage = gottenIntent.getStringExtra("COMING_FROM");

        Intent intent = new Intent(this, MainActivity.class);

        switch(previousPage) {
            case "MainActivity":
                intent = new Intent(this, MainActivity.class);
                break;
            case "MovieDetail":
                intent = new Intent(this, MovieDetailActivity.class);
                break;
        }
        intent.putExtra("value", movie);


        startActivity(intent);
    }

    public void addMovieIdToList(String listName, int movieId) {
        // Retrieve the current movieIdList string from the database
        LiveData<String> movieIdListLiveData = movieListViewModel.getMovieIdList(listName);
        movieIdListLiveData.observe(this, movieIdList -> {
            if (movieIdList != null) {
                // Convert the retrieved string into a list of integers
                List<Integer> movieIdListInt = IntegerListConverter.fromString(movieIdList);

                if (movieIdListInt == null) {
                    movieIdListInt = new ArrayList<>(); // Initialize the list if null
                }

                // Append the current movie ID to the list of integers

                    if(!movieIdListInt.contains(movieId)) {
                        movieIdListInt.add(movieId);
                    }
                    else{
                        Log.v("AddToListActivity", "Already in list" + String.valueOf(movieId));
                    }


                // Convert the updated list of integers back to a string
                String updatedMovieIdList = IntegerListConverter.fromList(movieIdListInt);

                // Update the movieIdList in the database with the updated string
                movieListViewModel.updateMovieIdList(updatedMovieIdList, listName);
            }
        });
    }

}

// Doel = MovieList tabel de movieId appenden
// 1. Van tabel MovieList een lijst met de String van alle movieId's
// 2. Convert die String naar lijst met integers
// 3. Append aan 1 of meer lijsten de huidige movieID

// movieIdList = ["[11,34]", "[21,21]"]
//