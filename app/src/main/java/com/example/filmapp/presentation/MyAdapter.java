package com.example.filmapp.presentation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.activities.AddToListActivity;
import com.example.filmapp.activities.MainActivity;
import com.example.filmapp.application.RecyclerViewInterface;
import com.example.filmapp.application.viewmodel.GenreViewModel;
import com.example.filmapp.R;
import com.example.filmapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Movie> movies;
    String genreName;
    GenreViewModel genreViewModel;
    private LifecycleOwner lifecycleOwner;

    private final RecyclerViewInterface recyclerViewInterface;

    public MyAdapter(Context context, List<Movie> movies, RecyclerViewInterface recyclerViewInterface, GenreViewModel genreViewModel, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.movies = movies;
        this.recyclerViewInterface = recyclerViewInterface;
        this.genreViewModel = genreViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public void setFilteredList(List<Movie> filteredList) {
        this.movies = filteredList;
        notifyDataSetChanged();
        Log.d("bbbb", "it set");


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.film_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Movie movie = movies.get(position);

        holder.titleView.setText(movie.getTitle());
        holder.releaseYearView.setText(movie.getReleaseDate());

        StringBuilder stringBuilder = new StringBuilder();
        List<Integer> genreIdList = movie.getGenreIdList();
        int totalCallbacksExpected = genreIdList.size();
        AtomicInteger callbacksCompleted = new AtomicInteger(0);

        for (int genreId : genreIdList) {
            genreViewModel.getGenreName(genreId).observe(lifecycleOwner, genreName -> {
//                Log.v("Adapter", " " + genreId);
                stringBuilder.append(genreName).append(", ");

                int callbacksCompletedSoFar = callbacksCompleted.incrementAndGet();
                if (callbacksCompletedSoFar == totalCallbacksExpected) {
                    // All callbacks completed, update UI with concatenated genre names
                    String genreNamesString = stringBuilder.toString();
                    // Remove trailing comma
                    if (genreNamesString.endsWith(", ")) {
                        genreNamesString = genreNamesString.substring(0, genreNamesString.length() - 2);
                    }
                    holder.genreView.setText(genreNamesString);
                    // Update your UI here if needed
                }
            });
        }

        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getImagePath()).into(holder.imageView);
        holder.itemView.setOnClickListener(v -> recyclerViewInterface.onItemClick(movie));

        holder.imageButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.imageButton);

            popupMenu.getMenuInflater().inflate(R.menu.menu_movie_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Intent intent = new Intent(context, AddToListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MOVIE", movie);
                context.startActivity(intent);
//                Toast.makeText(context, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });

            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
    if (movies != null){
        return movies.size();
    } else {
        return 0;
    }

    }
}
