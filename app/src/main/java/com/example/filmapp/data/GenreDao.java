package com.example.filmapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.filmapp.model.Genre;

import java.util.List;

@Dao
public interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertGenre(Genre... genre);

    @Query("SELECT * FROM Genre")
    LiveData<List<Genre>> getAllGenres();

    @Query("SELECT Name FROM Genre WHERE id = :genreId")
    LiveData<String> getGenreName(int genreId);
}
