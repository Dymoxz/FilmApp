package com.example.filmapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView nameView,listSizeView;
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.listIcon);
        nameView = itemView.findViewById(R.id.listName);
        listSizeView = itemView.findViewById(R.id.listSize);
    }
}
