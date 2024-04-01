package com.example.filmapp.data;

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

    @Query("SELECT * FROM Movie ORDER BY rating DESC")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT (SELECT COUNT(*) FROM Movie) == 0")
    LiveData<Boolean> moviesIsEmpty();

    @Query("SELECT imagePath FROM Movie WHERE id = :movieId")
    LiveData<String> getImagePath(int movieId);

}
