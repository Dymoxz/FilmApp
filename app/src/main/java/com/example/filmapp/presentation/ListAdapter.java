package com.example.filmapp.presentation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                    movieIdListInt = new ArrayList<>(); // Initialize the list if null
                }
                amountLiveData.setValue(String.valueOf(movieIdListInt.size()));
            }
        });
        amountLiveData.observe(lifecycleOwner, amount -> {
            if (amount != null) {
                holder.listSizeView.setText(amount);
            } else {
                holder.listSizeView.setText("0");
            }
        });


        if (movieList.getName().equals("Favorites")) {
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
