package com.example.filmapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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


    @Query("SELECT movieIdList FROM MovieList WHERE name = :listName")
    LiveData<String> getMovieIdList(String listName);

    @Query("UPDATE MovieList SET movieIdList = :movieIdList WHERE name = :listName")
    void insertMovieIdList(String movieIdList, String listName);
}
