package com.example.filmapp.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ListDetailActivity extends AppCompatActivity implements RecyclerViewInterface {
    String listName;
    TextView amountView;
    TextView listTextView;

    private MovieListViewModel movieListViewModel;
    private MovieViewModel movieViewModel;
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView searchView;
    private GenreViewModel genreViewModel;
    private LinearLayout linearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        searchView = findViewById(R.id.listSearchBar);
        searchView.clearFocus();
        amountView = findViewById(R.id.amount);
        listTextView = findViewById(R.id.listTitle);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        initViewModel();

        try {
            listName = intent.getStringExtra("listName");
            Log.v("ListDetailActivity", "List Name: " + listName);
        } catch (Exception e) {
            Log.v("ERROR", "no list found" + e);
        }

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
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }

        if (listName != null) {
            listTextView.setText(listName);
        }

        getAmountOfMovies().observe(this, amount -> {
            if (amount != null) {
                amountView.setText("Movies: " + amount);
            } else {
                amountView.setText("0");
            }
        });

        setMovieList();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void filterList(String text) {
        List<Movie> filteredList = new ArrayList<>();
        if (movieList != null) {
            for (Movie movie : movieList) {
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
                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredList, ListDetailActivity.this, genreViewModel, ListDetailActivity.this, getClass().getSimpleName());
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            shareList();
        } else if (item.getItemId() == R.id.delete) {
            deleteList();
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

    public void initViewModel() {
        MovieListRepository repository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
        movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
        movieListViewModel.init(repository);

        Repository movieRepository = new Repository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieDao());
        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieViewModel.class);
        movieViewModel.init(movieRepository);

        GenreRepository genreRepository = new GenreRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).genreDao());
        genreViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(GenreViewModel.class);
        genreViewModel.init(genreRepository);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.v("AddToListActivity", "Trying to remove a movie from the list");
            int position = viewHolder.getAdapterPosition();
            Movie movie = movieList.get(position);
            int movieId = movie.getId(); // Assuming getId() retrieves the movie id

            Log.v("AddToListActivity", "Trying to remove movie " + movieId + " from the list");
            removeMovieIdFromList(listName, movieId);

            movieList.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);

            if (movieList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
            }

            Snackbar snackbar = Snackbar.make(linearLayout, "Item removed from the list!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete from list")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_outline_24)
                    .setActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_error))
                    .create()
                    .decorate();

            if (movieList.isEmpty()) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void removeMovieIdFromList(String listName, int movieId) {
        LiveData<String> movieIdListLiveData = movieListViewModel.getMovieIdList(listName);
        movieIdListLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String movieIdList) {
                if (movieIdList != null) {
                    Log.v("AddToListActivity", "movieIdList = " + movieIdList);
                    Log.v("AddToListActivity", "movieId = " + movieId);

                    List<Integer> movieIdListInt = IntegerListConverter.fromString(movieIdList);

                    if (movieIdListInt == null) {
                        movieIdListInt = new ArrayList<>();
                    }

                    boolean removed = movieIdListInt.remove(Integer.valueOf(movieId)); // Remove the movieId from the list

                    if (removed) {
                        Log.v("AddToListActivity", "Removed " + movieId + " from the list");
                        String updatedMovieIdList = IntegerListConverter.fromList(movieIdListInt);

                        movieListViewModel.updateMovieIdList(updatedMovieIdList, listName);
                        movieIdListLiveData.removeObserver(this);
                    } else {
                        Log.v("AddToListActivity", movieId + " is not in the list");
                    }
                }
            }
        });
    }

    public LiveData<String> getAmountOfMovies() {
        if (movieListViewModel == null) {
            MovieListRepository repository = new MovieListRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieListDao());
            movieListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieListViewModel.class);
            movieListViewModel.init(repository);
        }

        MutableLiveData<String> amountLiveData = new MutableLiveData<>();
        LiveData<String> movieIdListLiveData = movieListViewModel.getMovieIdList(listName);
        movieIdListLiveData.observe(this, movieIdList -> {
            if (movieIdList != null) {
                List<Integer> movieIdListInt = IntegerListConverter.fromString(movieIdList);
                if (movieIdListInt == null) {
                    movieIdListInt = new ArrayList<>();
                }
                amountLiveData.setValue(String.valueOf(movieIdListInt.size()));
            }
        });

        return amountLiveData;

    }

    public void deleteList(){
        if (listName.equals("Favorites") || listName.equals("Watch later")){
            Toast.makeText(this, "Cannot delete " + listName, Toast.LENGTH_SHORT).show();
        }
        else {
            movieListViewModel.deleteMovieList(listName);
            Log.v("ListDetailActivity", "deleted list with name " + listName);
            changeActivityToListDetail();
        }
    }

    public void shareList() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        StringBuilder shareBuilder = new StringBuilder();
        shareBuilder.append("Check out this list: \n")
                    .append("*").append(listName).append("*\n");

        for (Movie movie : movieList) {
            shareBuilder.append(movie.getTitle()).append("\n");
        }

        String shareBody = String.valueOf(shareBuilder);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share App Locker"));
    }

    public void setMovieList(){
        LiveData<String> movieIdListLiveData = movieListViewModel.getMovieIdList(listName);

        movieIdListLiveData.observe(this, movieIdList -> {
            if (movieIdList != null) {
                // Convert the retrieved string into a list of integers
                List<Integer> movieIdListInt = IntegerListConverter.fromString(movieIdList);
                if (movieIdListInt == null) {
                    movieIdListInt = new ArrayList<>(); // Initialize the list if null
                }
                MediatorLiveData<List<Movie>> mediatorLiveData = new MediatorLiveData<>();
                for (int id : movieIdListInt) {
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
                    MyAdapter adapter = new MyAdapter(getApplicationContext(), movieList, this, genreViewModel, this, getClass().getSimpleName());
                    recyclerView.setAdapter(adapter);
                });
            }
        });
    }

}









