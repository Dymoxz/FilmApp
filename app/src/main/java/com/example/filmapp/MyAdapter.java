package com.example.filmapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.Application.RecyclerViewInterface;
import com.example.filmapp.Application.repository.GenreRepository;
import com.example.filmapp.Application.viewmodel.GenreViewModel;
import com.example.filmapp.Data.Database;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;
import com.squareup.picasso.Picasso;

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
                Log.v("Adapter", " " + genreId);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterface.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
