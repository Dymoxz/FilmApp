package com.example.filmapp.application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmapp.application.repository.GenreRepository;
import com.example.filmapp.model.Genre;

import java.util.List;

public class GenreViewModel extends ViewModel {
    private GenreRepository repository;
    private LiveData<List<Genre>> listGenres;

    private LiveData<String> genreName;

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
    public LiveData<String> getGenreName(int genreId) {
        return repository.getGenreName(genreId);
    }
    public void insertGenre(Genre genre) {
        repository.insertGenre(genre);
    }


}
