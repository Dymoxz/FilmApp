package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;
import com.example.filmapp.application.ListRecyclerViewInterface;
import com.example.filmapp.application.repository.MovieListRepository;
import com.example.filmapp.application.viewmodel.MovieListViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.model.MovieList;
import com.example.filmapp.presentation.ListAdapter;

import java.util.List;

public class AddToListActivity extends AppCompatActivity implements ListRecyclerViewInterface {
    private MovieListViewModel movieListViewModel;
    private List<MovieList> movieLists;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        intent.getSerializableExtra("MOVIE");

        MovieListRepository repository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
        movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
        movieListViewModel.init(repository);
        movieListViewModel.getMovieLists().observe(this, movieListsLiveData -> {
            if (movieListsLiveData != null) {
                movieLists = movieListsLiveData; // Assuming movieLists is a List<MovieList> variable
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new ListAdapter(getApplicationContext(), movieLists, AddToListActivity.this));
            }
        });
    }

    @Override
    public void onItemClick(MovieList movieList) {

    }
}
