package com.example.filmapp.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.filmapp.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insertMovie(Movie... movies);

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getAllMovies();
}
