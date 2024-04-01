package com.example.filmapp.presentation;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;
import com.example.filmapp.model.Movie;

import java.util.List;

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
