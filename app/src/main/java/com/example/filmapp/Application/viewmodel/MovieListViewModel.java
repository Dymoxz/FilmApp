package com.example.filmapp.Application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmapp.Application.repository.MovieListRepository;
import com.example.filmapp.model.MovieList;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieListRepository repository;
    private LiveData<List<MovieList>> listMovieLists;

    public void init(MovieListRepository repository) {
        if (this.repository != null) {
            // ViewModel is already initialized
            return;
        }
        this.repository = repository;
        this.listMovieLists = repository.getAllMovieLists();
    }

    public LiveData<List<MovieList>> getMovieLists() {
        return listMovieLists;
    }

    public void insertMovieList(MovieList movieList) {
        repository.insertMovieList(movieList);
    }


}
