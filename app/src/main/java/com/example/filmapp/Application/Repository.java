package com.example.filmapp.Application;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.filmapp.Data.Database;
import com.example.filmapp.Data.MovieDao;
import com.example.filmapp.model.Movie;

import java.util.List;

public class Repository {

    private Database database;
    private MovieDao movieDao;
    private LiveData<List<Movie>> listMovies;

    public Repository(Database database, MovieDao movieDao) {
        this.database = database;
        this.movieDao = movieDao;
        this.listMovies = movieDao.getAllMovies(); // Now correctly assigns LiveData<List<Movie>>
    }

    public LiveData<List<Movie>> getAllMovies() {
        return listMovies;
    }

    void insertMovie(Movie movie) {
        database.databaseWriteExecutor.execute(() -> movieDao.insertMovie(movie));
    }

}

