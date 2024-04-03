package com.example.filmapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.filmapp.presentation.AddToListViewHolder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
@TypeConverters({IntegerListConverter.class})
@Entity(tableName = "MovieList")
public class MovieList implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    @Ignore
    private boolean selected;
    public List<Integer> getMovieIdList() {
        return movieIdList;
    }

    public void setMovieIdList(List<Integer> movieIdList) {
        this.movieIdList = movieIdList;
    }

    @SerializedName("movie_ids")
    private List<Integer> movieIdList;

    public MovieList(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
