package com.example.filmapp.application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmapp.application.repository.MovieListRepository;
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
    public void deleteMovieList(String movieListName) {
        repository.deleteMovieList(movieListName);
    }

    public LiveData<String> getMovieIdList(String movieListName) {
        return repository.getMovieIdList(movieListName);
    }

    public void updateMovieIdList(String movieIdList, String listName) {
        repository.updateMovieIdList(movieIdList, listName);
    }

}
