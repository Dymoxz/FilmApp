package com.example.filmapp.Activities;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.Application.ListRecyclerViewInterface;
import com.example.filmapp.Application.repository.MovieListRepository;
import com.example.filmapp.Application.viewmodel.MovieListViewModel;
import com.example.filmapp.Data.Database;
import com.example.filmapp.ListAdapter;
import com.example.filmapp.R;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.MovieList;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ListsActivity extends AppCompatActivity implements ListRecyclerViewInterface {

    private MovieListViewModel movieListViewModel;
    private List<MovieList> movieLists;
    private String createListName = "";
    String createListDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lists);

        MovieListRepository repository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
        movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
        movieListViewModel.init(repository);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);

        }

        movieListViewModel.getMovieLists().observe(this, movieListsLiveData -> {
            if (movieListsLiveData != null) {
                movieLists = movieListsLiveData; // Assuming movieLists is a List<MovieList> variable
                RecyclerView recyclerView = findViewById(R.id.recyclerViewList);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new ListAdapter(getApplicationContext(), movieLists));
            }
        });



    }
    public void changeActivityToMovies(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createList(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please submit a list name: ");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createListName = input.getText().toString();
                createListDate = LocalDate.now().toString();
                MovieList movieList = new MovieList(createListName);
                Log.v("listActivity", "Created list with name '" + createListName + "'");
                movieListViewModel.insertMovieList(movieList);
                changeActivityToListDetail();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Log.v("listActivity", "canceled list creation");
            }
        });

        builder.show();
    }

    public void changeActivityToListDetail(){
        Intent intent = new Intent(this, ListDetailActivity.class);
        intent.putExtra("listName", createListName);
        intent.putExtra("listDate", createListDate);
        startActivity(intent);
    }

    @Override
    public void onItemClick(MovieList movieList) {
        Intent intent = new Intent(this, ListDetailActivity.class);
        intent.putExtra("listName", movieList.getName());
        intent.putExtra("listDate", createListDate);
        startActivity(intent);
    }
}
