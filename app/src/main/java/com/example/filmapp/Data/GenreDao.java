package com.example.filmapp.Data;

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
    void insertGenre(Genre genre);

    @Query("SELECT * FROM Genre")
    LiveData<List<Genre>> getAllGenres();
}
