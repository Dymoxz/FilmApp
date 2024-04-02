package com.example.filmapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.filmapp.model.Genre;
import com.example.filmapp.model.MovieReview;

import java.util.List;

@Dao
public interface MovieReviewDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovieReview(MovieReview... movieReview);

    @Query("SELECT * FROM MovieReview")
    LiveData<List<MovieReview>> getAllMovieReviews();

    @Query("SELECT * FROM MovieReview WHERE movieId == :movie  ")
    LiveData<List<MovieReview>> getAllReviewsForMovie(int movie);
}
