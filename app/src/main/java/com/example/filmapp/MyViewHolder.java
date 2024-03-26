package com.example.filmapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleView,releaseYearView, genreView;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.filmListImage);
        titleView = itemView.findViewById(R.id.filmListTitle);
        genreView = itemView.findViewById(R.id.filmListGenre);
        releaseYearView = itemView.findViewById(R.id.filmListReleaseYear);
    }
}
