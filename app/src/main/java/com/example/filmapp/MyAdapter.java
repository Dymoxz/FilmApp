package com.example.filmapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Movie> movies;

    public MyAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.film_item_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleView.setText(movie.getTitle());
        holder.releaseYearView.setText(movie.getReleaseDate());
        holder.genreView.setText(String.valueOf(movie.getGenreIdList().get(0)));
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getImagePath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
