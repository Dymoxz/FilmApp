package com.example.filmapp.presentation;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;
import com.example.filmapp.activities.AddToListActivity;
import com.example.filmapp.application.ListRecyclerViewInterface;
import com.example.filmapp.model.MovieList;

import java.util.ArrayList;
import java.util.List;

public class AddToListAdapter extends RecyclerView.Adapter<AddToListViewHolder> {
    Context context;
    List<MovieList> movieListList;
    ListRecyclerViewInterface recyclerViewInterface;

    public AddToListAdapter(Context context, List<MovieList> movieListList, ListRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.movieListList = movieListList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AddToListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.add_to_list_item_row, parent, false);
        return new AddToListViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull AddToListViewHolder holder, int position) {
        MovieList movieList = movieListList.get(position);
        holder.nameView.setText(movieList.getName());

        // Set the state of RadioButton based on selection
        holder.radioButton.setChecked(movieList.isSelected());

        if (movieList.getName().equals("Favorites")) {
            holder.imageView.setImageResource(R.drawable.baseline_favorite_24);
        }
        else if (movieList.getName().equals("Watch later")) {
            holder.imageView.setImageResource(R.drawable.baseline_watch_later_24);
        }

        // Handle RadioButton click to toggle selection
        holder.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            movieList.setSelected(isChecked);
            notifyDataSetChanged(); // Notify adapter of item change
        });
    }

    @Override
    public int getItemCount() {
        return movieListList != null ? movieListList.size() : 0;
    }

    // Method to get the selected list names
    public List<String> getSelectedListNames() {
        List<String> selectedListNames = new ArrayList<>();
        for (MovieList movieList : movieListList) {
            if (movieList.isSelected()) {
                selectedListNames.add(movieList.getName());
            }
        }
        return selectedListNames;
    }
}


