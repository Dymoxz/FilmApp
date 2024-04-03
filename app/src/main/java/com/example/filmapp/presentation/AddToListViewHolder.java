package com.example.filmapp.presentation;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;

public class AddToListViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView nameView;
    RadioButton radioButton;
    public AddToListViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.listIcon);
        nameView = itemView.findViewById(R.id.listName);
        radioButton = itemView.findViewById(R.id.button);
    }
}
