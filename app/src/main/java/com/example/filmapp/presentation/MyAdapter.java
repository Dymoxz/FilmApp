package com.example.filmapp.presentation;

import android.app.Activity;
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

    GenreViewModel genreViewModel;

    private LifecycleOwner lifecycleOwner;

    private String parentActivityType;

    private final RecyclerViewInterface recyclerViewInterface;

    public MyAdapter(Context context, List<Movie> movies, RecyclerViewInterface recyclerViewInterface, GenreViewModel genreViewModel, LifecycleOwner lifecycleOwner, String parentActivityType) {
        this.context = context;
        this.movies = movies;
        this.recyclerViewInterface = recyclerViewInterface;
        this.genreViewModel = genreViewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.parentActivityType = parentActivityType;
    }

    public void setFilteredList(List<Movie> filteredList) {
        this.movies = filteredList;
        notifyDataSetChanged();
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
                stringBuilder.append(genreName).append(", ");

                int callbacksCompletedSoFar = callbacksCompleted.incrementAndGet();
                if (callbacksCompletedSoFar == totalCallbacksExpected) {

                    String genreNamesString = stringBuilder.toString();

                    if (genreNamesString.endsWith(", ")) {
                        genreNamesString = genreNamesString.substring(0, genreNamesString.length() - 2);
                    }

                    holder.genreView.setText(genreNamesString);
                }
            });
        }

        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getImagePath()).into(holder.imageView);
        holder.itemView.setOnClickListener(v -> recyclerViewInterface.onItemClick(movie));

        holder.imageButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.imageButton);

            if ("ListDetailActivity".equals(parentActivityType)) {
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_list_movie, popupMenu.getMenu());
            } else {
                popupMenu.getMenuInflater().inflate(R.menu.menu_movie_item, popupMenu.getMenu());
            }

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.addToList) {
                    Intent intent = new Intent(context, AddToListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("MOVIE", movie);
                    intent.putExtra("COMING_FROM", "MainActivity");
                    context.startActivity(intent);
                    return true;
                }
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
