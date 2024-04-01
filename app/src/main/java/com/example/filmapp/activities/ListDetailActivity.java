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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.application.RecyclerViewInterface;
import com.example.filmapp.application.repository.GenreRepository;
import com.example.filmapp.application.repository.MovieListRepository;
import com.example.filmapp.application.repository.Repository;
import com.example.filmapp.application.viewmodel.GenreViewModel;
import com.example.filmapp.application.viewmodel.MovieListViewModel;
import com.example.filmapp.application.viewmodel.MovieViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.R;
import com.example.filmapp.model.IntegerListConverter;
import com.example.filmapp.model.Movie;
import com.example.filmapp.presentation.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListDetailActivity extends AppCompatActivity implements RecyclerViewInterface {
    String listName;

    TextView listTextView;

    private MovieListViewModel movieListViewModel;
    private MovieViewModel movieViewModel;
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GenreViewModel genreViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        listTextView = findViewById(R.id.listTitle);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GenreRepository genreRepository = new GenreRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).genreDao());
        genreViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(GenreViewModel.class);
        genreViewModel.init(genreRepository);

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
        initViewModel();
        LiveData<String> movieIdListLiveData = movieListViewModel.getMovieIdList(listName);
        movieIdListLiveData.observe(this, movieIdList -> {
            if (movieIdList != null) {
                // Convert the retrieved string into a list of integers
                List<Integer> movieIdListInt = IntegerListConverter.fromString(movieIdList);
                if (movieIdListInt == null) {
                    movieIdListInt = new ArrayList<>(); // Initialize the list if null
                }
                MediatorLiveData<List<Movie>> mediatorLiveData = new MediatorLiveData<>();
                for (int id : movieIdListInt){
                    LiveData<Movie> movieLiveData = movieViewModel.getMovie(id);
                    mediatorLiveData.addSource(movieLiveData, movie -> {
                        List<Movie> newList = mediatorLiveData.getValue();
                        if (newList == null) {
                            newList = new ArrayList<>();
                        }
                        newList.add(movie);
                        mediatorLiveData.setValue(newList);
                    });
                }

                mediatorLiveData.observe(this, movies -> {
                    movieList.clear();
                    movieList.addAll(movies);
                    MyAdapter adapter = new MyAdapter(getApplicationContext(), movieList, this, genreViewModel, this);
                    recyclerView.setAdapter(adapter);
                });
            }
        });

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

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("value", movie);
        intent.putExtras(bundle);
        Log.d("ListDetailActivity", "clicked on movie: " + movie.getTitle());
        startActivity(intent);
    }

    public void initViewModel(){
        MovieListRepository repository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
        movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
        movieListViewModel.init(repository);
        Repository movieRepository = new Repository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieDao());
        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieViewModel.class);
        movieViewModel.init(movieRepository);
    }
}

