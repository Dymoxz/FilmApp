package com.example.filmapp.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.MovieList;

import java.util.List;

public class GenreViewModel extends ViewModel {
    private GenreRepository repository;
    private LiveData<List<Genre>> listGenres;

    public void init(GenreRepository repository) {
        if (this.repository != null) {
            // ViewModel is already initialized
            return;
        }
        this.repository = repository;
        this.listGenres = repository.getAllGenres();
    }

    public LiveData<List<Genre>> getListGenres() {
        return listGenres;
    }

    public void insertGenre(Genre genre) {
        repository.insertGenre(genre);
    }


}
