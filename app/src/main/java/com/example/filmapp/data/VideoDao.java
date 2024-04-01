package com.example.filmapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertVideo(Video... video);

    @Query("SELECT * FROM Video")
    LiveData<List<Video>> getAllVideos();

    @Query("SELECT * FROM Video WHERE movieId = :movieId")
    LiveData<Video> getVideo(int movieId);
}
