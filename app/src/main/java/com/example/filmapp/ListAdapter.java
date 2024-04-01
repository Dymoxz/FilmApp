package com.example.filmapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.Application.ListRecyclerViewInterface;
import com.example.filmapp.Application.RecyclerViewInterface;
import com.example.filmapp.model.MovieList;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    Context context;
    List<MovieList> movieListList;
    ListRecyclerViewInterface recyclerViewInterface;

    public ListAdapter(Context context, List<MovieList> movieListList, ListRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.movieListList = movieListList;
        this.recyclerViewInterface = recyclerViewInterface;

    }


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
        if (movieList.getName().equals("cool list")) {
            holder.imageView.setImageResource(R.drawable.baseline_format_list_bulleted_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewInterface !=null) {
                    recyclerViewInterface.onItemClick(movieList);
                }
                else {
                    Log.v("aaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaa");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieListList != null ? movieListList.size() : 0;
    }

}
