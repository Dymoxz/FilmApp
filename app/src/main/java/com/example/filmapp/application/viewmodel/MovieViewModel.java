package com.example.filmapp.application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmapp.application.repository.Repository;
import com.example.filmapp.model.Movie;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private Repository repository;
    private LiveData<List<Movie>> listMovies;

    public void init(Repository repository) {
        if (this.repository != null) {
            // ViewModel is already initialized
            return;
        }
        this.repository = repository;
        this.listMovies = repository.getAllMovies();
    }

    public LiveData<List<Movie>> getListMovies() {
        return listMovies;
    }
    public LiveData<Movie> getMovie(int movieId) {
        return repository.getMovie(movieId);
    }
    public void insertMovie(Movie movie) {
        repository.insertMovie(movie);
    }

    public LiveData<Boolean> moviesIsEmpty() {
        return repository.moviesIsEmpty();
    }

    public LiveData<String> getImagePath(int movieId) { return repository.getImagePath(movieId); }
}