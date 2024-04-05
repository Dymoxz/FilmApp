package com.example.filmapp.presentation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.activities.AddToListActivity;
import com.example.filmapp.application.ListRecyclerViewInterface;
import com.example.filmapp.R;
import com.example.filmapp.application.viewmodel.MovieListViewModel;
import com.example.filmapp.model.IntegerListConverter;
import com.example.filmapp.model.MovieList;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    Context context;
    List<MovieList> movieListList;
    ListRecyclerViewInterface recyclerViewInterface;
    MovieListViewModel movieListViewModel; // Add this line
    LifecycleOwner lifecycleOwner;

    public ListAdapter(Context context, List<MovieList> movieListList, ListRecyclerViewInterface recyclerViewInterface, MovieListViewModel movieListViewModel, LifecycleOwner lifecycleOwner) { // Modify this line
        this.context = context;
        this.movieListList = movieListList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.movieListViewModel = movieListViewModel; // Add this line
        this.lifecycleOwner = lifecycleOwner;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;

        // Check if the parent activity is AddToListActivity or ListActivity
        if (parent.getContext() instanceof AddToListActivity) {
            // Inflate the layout for AddToListActivity
            itemView = inflater.inflate(R.layout.add_to_list_item_row, parent, false);
        } else {
            // Inflate the layout for ListActivity
            itemView = inflater.inflate(R.layout.list_item_row, parent, false);
        }

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        MovieList movieList = movieListList.get(position);
        holder.nameView.setText(movieList.getName());

        MutableLiveData<String> amountLiveData = new MutableLiveData<>();
        LiveData<String> movieCountLiveData = movieListViewModel.getMovieIdList(movieList.getName());
        movieCountLiveData.observe(lifecycleOwner, movieIdList -> {
            if (movieIdList != null) {
                List<Integer> movieIdListInt = IntegerListConverter.fromString(movieIdList);
                if (movieIdListInt == null) {
                    movieIdListInt = new ArrayList<>();
                }
                amountLiveData.setValue(String.valueOf(movieIdListInt.size()));
            }
        });
        amountLiveData.observe(lifecycleOwner, amount -> {
            if (amount != null) {
                holder.listSizeView.setText("Movies: " + amount);
            } else {
                holder.listSizeView.setText("0");
            }
        });


        if (movieList.getName().equals("Favorites")) {
            holder.imageView.setImageResource(R.drawable.baseline_favorite_24);
        }
        else if (movieList.getName().equals("Watch later")) {
            holder.imageView.setImageResource(R.drawable.baseline_watch_later_24);
        }

        // click listener for ellipse
        holder.movieItemButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.movieItemButton);
            popupMenu.inflate(R.menu.list_menu_item);
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.deleteList) {
                    if (movieList.getName().equals("Favorites") || movieList.getName().equals("Watch later")){
                        Toast.makeText(context, "Cannot delete " + movieList.getName(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.v("ListAdapter", "List has successfully been deleted");
                        handleDeleteOption(movieList);
                        Toast.makeText(context, movieList.getName() + "has successfully been deleted", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            });
            popupMenu.show();
        });

        holder.itemView.setOnClickListener(v -> {
            if (recyclerViewInterface !=null) {
                recyclerViewInterface.onItemClick(movieList);
            }
            else {
                Log.v("aaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaa");
            }
        });
    }

    // ellipse functions
    private void handleDeleteOption(MovieList movieList) {
        movieListViewModel.deleteMovieList(movieList.getName());

    }

    @Override
    public int getItemCount() {
        return movieListList != null ? movieListList.size() : 0;
    }

}
