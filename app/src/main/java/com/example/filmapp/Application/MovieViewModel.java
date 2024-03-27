package com.example.filmapp.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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
}