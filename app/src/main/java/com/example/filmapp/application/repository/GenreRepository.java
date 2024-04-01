package com.example.filmapp.application.repository;

import androidx.lifecycle.LiveData;

import com.example.filmapp.data.Database;
import com.example.filmapp.data.GenreDao;
import com.example.filmapp.model.Genre;

import java.util.List;

public class GenreRepository {

    private Database database;
    private GenreDao genreDao;
    private LiveData<List<Genre>> listGenres;


    public GenreRepository(Database database, GenreDao genreDao) {
        this.database = database;
        this.genreDao = genreDao;
        this.listGenres = genreDao.getAllGenres();

    }


    public void insertGenre(Genre genre) {
        database.databaseWriteExecutor.execute(() -> genreDao.insertGenre(genre));
    }

    public LiveData<List<Genre>> getAllGenres() {
        return listGenres;
    }
    public LiveData<String> getGenreName(int genreId) {
        return genreDao.getGenreName(genreId);
    }

}

