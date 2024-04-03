package com.example.filmapp.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;
import com.example.filmapp.model.CastMember;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastMemberAdapter extends RecyclerView.Adapter<CastMemberViewHolder> {
    private List<CastMember> castMembers;

    public CastMemberAdapter(List<CastMember> castMembers) {
        this.castMembers = castMembers;
    }

    @NonNull
    @Override
    public CastMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_member_item_row, parent, false);
        return new CastMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastMemberViewHolder holder, int position) {
        CastMember castMember = castMembers.get(position);
        // Bind data to views in holder
        holder.castMemberName.setText(castMember.getName());
        holder.castMemberPlayingAs.setText("Playing as: " + castMember.getCharacter());
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + castMember.getProfile_path()).into(holder.castMemberImage);

    }

    @Override
    public int getItemCount() {
        return castMembers.size();
    }
}