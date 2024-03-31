package com.example.filmapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.model.Movie;

import java.util.List;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleView,releaseYearView, genreView;
    List<Movie> movies;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.filmListImage);
        titleView = itemView.findViewById(R.id.filmListTitle);
        genreView = itemView.findViewById(R.id.filmListGenre);
        releaseYearView = itemView.findViewById(R.id.filmListReleaseYear);
    }

//    @Override
//    public void onClick(View view) {
//        int position = getAdapterPosition();
//        Log.d("Adapter", "List item onClick: position " + position);
//
//        Movie movie = movies.get(position);
//        Log.d("Adapter", "Cocktail ID: " + movie.getId());
//
//        Intent intent = new Intent(view.getContext(), ListDetailActivity.class);
//        intent.putExtra("EXTRA_ADDED_MOVIE", movie);
//        view.getContext().startActivity(intent);
//    }
}
