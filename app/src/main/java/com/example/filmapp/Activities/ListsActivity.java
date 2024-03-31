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

import com.example.filmapp.Application.repository.MovieListRepository;
import com.example.filmapp.Application.viewmodel.MovieListViewModel;
import com.example.filmapp.Data.Database;
import com.example.filmapp.R;
import com.example.filmapp.model.MovieList;

import java.time.LocalDate;
import java.util.List;

public class ListsActivity extends AppCompatActivity {

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

//        INSERT A MOVIELIST INTO THE ROOM DATABASE:
//        MovieList favorites = new MovieList(1, "Favorites");
//        movieListViewModel.insertMovieList(favorites);
//        Log.v("ListsActivity", "inserted movie " + favorites.getName());

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
                Log.v("listActivity", "Created list with name '" + createListName + "'");
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
}
