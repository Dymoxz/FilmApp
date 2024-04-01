package com.example.filmapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.Application.RecyclerViewInterface;
import com.example.filmapp.model.MovieList;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    Context context;
    List<MovieList> movieListList;

    public ListAdapter(Context context, List<MovieList> movieListList) {
        this.context = context;
        this.movieListList = movieListList;

    }

    RecyclerViewInterface recyclerViewInterface;
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        MovieList movieList = movieListList.get(position);
        holder.nameView.setText(movieList.getName());
        holder.listSizeView.setText("0");
        holder.imageView.setImageResource(R.drawable.baseline_format_list_bulleted_24);
    }

    @Override
    public int getItemCount() {
        return movieListList != null ? movieListList.size() : 0;
    }

}
