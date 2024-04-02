package com.example.filmapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.application.ListRecyclerViewInterface;
import com.example.filmapp.application.repository.MovieListRepository;
import com.example.filmapp.application.viewmodel.MovieListViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.presentation.ListAdapter;
import com.example.filmapp.R;
import com.example.filmapp.model.MovieList;

import java.util.List;

public class ListsActivity extends AppCompatActivity implements ListRecyclerViewInterface {

    private MovieListViewModel movieListViewModel;
    private List<MovieList> movieLists;
    private String createListName = "";
    private List<String> movieNames;
    ListRecyclerViewInterface listRecyclerViewInterface;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lists);

        MovieListRepository repository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
        movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
        movieListViewModel.init(repository);

        //if (movieListsLiveData.isEmpty()){
        //                    MovieList favorites = new MovieList("Favorites");
        //                    MovieList watchLater = new MovieList("Watch later");
        //                    movieLists.add(favorites);
        //                    movieLists.add(watchLater);
        //                    Log.v("ListActivity", "Added favoires");
        //                }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);

        }

        movieListViewModel.getMovieLists().observe(this, movieListsLiveData -> {
            if (movieListsLiveData != null) {
                movieLists = movieListsLiveData;

                // Check if the movie lists are empty
//                if (movieLists.isEmpty()) {
//                    // Add default lists if the database is empty
//                    MovieList favorites = new MovieList("Favorites");
//                    MovieList watchLater = new MovieList("Watch later");
//                    movieLists.add(favorites);
//                    movieLists.add(watchLater);
//                    movieListViewModel.insertMovieList(favorites);
//                    movieListViewModel.insertMovieList(watchLater);
//
//                    Log.v("ListActivity", "Added Favorites and Watch later lists");
//                }

                // Set up the RecyclerView with the movie lists
                RecyclerView recyclerView = findViewById(R.id.recyclerViewList);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new ListAdapter(getApplicationContext(), movieLists, ListsActivity.this, movieListViewModel, this));
            }
        });


    }

    public void changeActivityToMovies(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createList(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please submit a list name: ");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            createListName = input.getText().toString();
            doesNameExist(createListName).observe(ListsActivity.this, nameExists -> {
                if (nameExists != null) {
                    if (!nameExists) {
                        MovieList movieList = new MovieList(createListName);
                        Log.v("ListActivity", "Created list with name '" + createListName + "'");
                        movieListViewModel.insertMovieList(movieList);
                        changeActivityToListDetail();
                    } else {
                        Log.v("ListActivity", createListName + " already exists");
                        // Show a message to the user indicating that the name already exists
                    }
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            Log.v("ListActivity", "Cancelled list creation");
        });

        builder.show();
    }

    public void changeActivityToListDetail() {
        Intent intent = new Intent(this, ListDetailActivity.class);
        intent.putExtra("listName", createListName);
        startActivity(intent);
    }

    @Override
    public void onItemClick(MovieList movieList) {
        Intent intent = new Intent(this, ListDetailActivity.class);
        intent.putExtra("listName", movieList.getName());
        startActivity(intent);
    }

    public LiveData<Boolean> doesNameExist(String inputMovieName) {
        MutableLiveData<Boolean> nameExistsLiveData = new MutableLiveData<>();

        movieListViewModel.getMovieNames().observe(this, movieNamesLiveData -> {
            if (movieNamesLiveData != null) {
                boolean nameExists = false;

                for (String movieName : movieNamesLiveData) {
                    if (inputMovieName.equals(movieName)) {
                        nameExists = true;
                        break;
                    }
                }

                nameExistsLiveData.setValue(nameExists);
            }
        });

        return nameExistsLiveData;
    }




}
