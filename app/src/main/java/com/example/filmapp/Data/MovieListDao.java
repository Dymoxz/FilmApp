package com.example.filmapp.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.filmapp.model.MovieList;

import java.util.List;


@Dao
public interface MovieListDao {

    @Insert
    void insertMovieList(MovieList... lists);

    @Query("SELECT * FROM MovieList")
    LiveData<List<MovieList>> getAllMovieLists();

    @Query("SELECT Name FROM MovieList")
    LiveData<List<String>> getAllMovieListNames();

    @Query("DELETE FROM MovieList WHERE name = :listName")
    void deleteMovieList(String listName);
}
