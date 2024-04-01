package com.example.filmapp.application.repository;

import androidx.lifecycle.LiveData;

import com.example.filmapp.data.Database;
import com.example.filmapp.data.GenreDao;
import com.example.filmapp.data.MovieDao;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Movie;

import java.util.List;

public class Repository {

    private Database database;
    private MovieDao movieDao;
    private GenreDao genreDao;
    private LiveData<List<Movie>> listMovies;
    private LiveData<Boolean> moviesIsEmpty;
    private LiveData<List<Genre>> listGenres;

    public Repository(Database database, MovieDao movieDao) {
        this.database = database;
        this.movieDao = movieDao;
        this.listMovies = movieDao.getAllMovies(); // Now correctly assigns LiveData<List<Movie>>
        this.moviesIsEmpty = movieDao.moviesIsEmpty();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return listMovies;
    }

    public LiveData<Boolean> moviesIsEmpty() { return moviesIsEmpty; }

    public void insertMovie(Movie movie) {
        database.databaseWriteExecutor.execute(() -> movieDao.insertMovie(movie));
    }

    public LiveData<String> getImagePath(int movieId) { return movieDao.getImagePath(movieId);}




}

