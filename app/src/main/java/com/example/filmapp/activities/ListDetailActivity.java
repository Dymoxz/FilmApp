package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.filmapp.application.repository.MovieListRepository;
import com.example.filmapp.application.viewmodel.MovieListViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.R;

public class ListDetailActivity extends AppCompatActivity {
    String listName;

    TextView listTextView;

    private MovieListViewModel movieListViewModel;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        listTextView = findViewById(R.id.listTitle);

        Intent intent = getIntent();
        try {
            listName = intent.getStringExtra("listName");
            Log.v("ListDetailActivity", "List Name: " + listName);
        } catch (Exception e){
            Log.v("ERROR", "no list found" + e);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set text to TextViews
        if (listName != null) {
            listTextView.setText(listName);
        }

        MovieListRepository repository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
        movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
        movieListViewModel.init(repository);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            // Handle share action
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = "createShareText(MovieList)";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Share App Locker"));
            return true;
        } else if (item.getItemId() == R.id.delete) {
            movieListViewModel.deleteMovieList(listName);
            Log.v("ListDetailActivity", "deleted list with name " + listName);
            changeActivityToListDetail();
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeActivityToListDetail() {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }
}
