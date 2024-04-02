package com.example.filmapp.application.repository;

import androidx.lifecycle.LiveData;

import com.example.filmapp.data.Database;
import com.example.filmapp.data.MovieListDao;
import com.example.filmapp.data.MovieReviewDao;
import com.example.filmapp.model.MovieList;
import com.example.filmapp.model.MovieReview;

import java.util.List;

public class MovieReviewRepository {
    private Database database;

    private MovieReviewDao movieReviewDao;

    private LiveData<List<MovieReview>> listMovieReviews;

    public MovieReviewRepository(Database database, MovieReviewDao movieReviewDao) {
        this.database = database;
        this.movieReviewDao = movieReviewDao;
        this.listMovieReviews = movieReviewDao.getAllMovieReviews();

    }

    public LiveData<List<MovieReview>> getAllMovieReviews() { return listMovieReviews; }

    public LiveData<List<MovieReview>> getReviewsForMovie(int movieId) {
        return movieReviewDao.getAllReviewsForMovie(movieId);
    }

    public void insertMovieReview(MovieReview movieReview) {
        database.databaseWriteExecutor.execute(() -> movieReviewDao.insertMovieReview(movieReview));
    }


}
