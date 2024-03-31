package com.example.filmapp.Application;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.filmapp.Data.Database;
import com.example.filmapp.Data.GenreDao;
import com.example.filmapp.Data.MovieDao;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;

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


    void insertGenre(Genre genre) {
        database.databaseWriteExecutor.execute(() -> genreDao.insertGenre(genre));
    }

    public LiveData<List<Genre>> getAllGenres() {
        return listGenres;
    }


}

