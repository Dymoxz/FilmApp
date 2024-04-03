package com.example.filmapp.presentation;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;

public class ListViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView nameView,listSizeView;
    ImageButton movieItemButton;
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.listIcon);
        nameView = itemView.findViewById(R.id.listName);
        listSizeView = itemView.findViewById(R.id.listSize);
        movieItemButton = itemView.findViewById(R.id.movieItemButton);
    }
}
