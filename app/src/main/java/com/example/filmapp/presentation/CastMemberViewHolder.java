package com.example.filmapp.presentation;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;

public class CastMemberViewHolder extends RecyclerView.ViewHolder {
    ImageView castMemberImage;
    TextView castMemberName;
    TextView castMemberPlayingAs;

    public CastMemberViewHolder(@NonNull View itemView) {
        super(itemView);
        castMemberImage = itemView.findViewById(R.id.castMemberImage);
        castMemberName = itemView.findViewById(R.id.castMemberName);
        castMemberPlayingAs = itemView.findViewById(R.id.castMemberPlayingAs);
    }
}