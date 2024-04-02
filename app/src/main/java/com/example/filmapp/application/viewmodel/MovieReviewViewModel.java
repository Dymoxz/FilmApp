package com.example.filmapp.application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmapp.application.repository.MovieListRepository;
import com.example.filmapp.application.repository.MovieReviewRepository;
import com.example.filmapp.model.MovieList;
import com.example.filmapp.model.MovieReview;

import java.util.List;

public class MovieReviewViewModel extends ViewModel {

    private MovieReviewRepository repository;
    private LiveData<List<MovieReview>> listMovieReviews;

    public void init(MovieReviewRepository repository) {
        if (this.repository != null) {
            // ViewModel is already initialized
            return;
        }
        this.repository = repository;
        this.listMovieReviews = repository.getAllMovieReviews();

    }

    public LiveData<List<MovieReview>> getAllMovieReviews() {
        return listMovieReviews;
    }

    public void insertMovieReview(MovieReview movieReview) {
        repository.insertMovieReview(movieReview);
    }

    public LiveData<List<MovieReview>> getReviewsForMovie(int movieId) {
        return repository.getReviewsForMovie(movieId);
    }



}
