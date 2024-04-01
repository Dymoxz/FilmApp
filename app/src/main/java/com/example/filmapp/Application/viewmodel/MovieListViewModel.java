package com.example.filmapp.Application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmapp.Application.repository.MovieListRepository;
import com.example.filmapp.model.MovieList;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieListRepository repository;
    private LiveData<List<MovieList>> listMovieLists;
    private LiveData<List<String>> MovieNamesList;
    public void init(MovieListRepository repository) {
        if (this.repository != null) {
            // ViewModel is already initialized
            return;
        }
        this.repository = repository;
        this.listMovieLists = repository.getAllMovieLists();
        this.MovieNamesList = repository.getAllMovieNames();
    }

    public LiveData<List<MovieList>> getMovieLists() {
        return listMovieLists;
    }
    public LiveData<List<String>> getMovieNames() {
        return MovieNamesList;
    }
    public void insertMovieList(MovieList movieList) {
        repository.insertMovieList(movieList);
    }


}
