package com.example.filmapp.application.repository;

import androidx.lifecycle.LiveData;

import com.example.filmapp.data.Database;
import com.example.filmapp.data.MovieListDao;
import com.example.filmapp.model.MovieList;

import java.util.List;

public class MovieListRepository {
    private Database database;

    private MovieListDao movieListDao;

    private LiveData<List<MovieList>> listMoviesList;

    private LiveData<List<String>> listMovieNamesList;
    public MovieListRepository(Database database, MovieListDao movieListDao) {
        this.database = database;
        this.movieListDao = movieListDao;
        this.listMoviesList = movieListDao.getAllMovieLists();
        this.listMovieNamesList = movieListDao.getAllMovieListNames();
    }

    public LiveData<List<MovieList>> getAllMovieLists() { return listMoviesList; }
    public LiveData<List<String>> getAllMovieNames() { return listMovieNamesList; }

    public void insertMovieList(MovieList movieList) {
        database.databaseWriteExecutor.execute(() -> movieListDao.insertMovieList(movieList));
    }

    public void deleteMovieList(String movieListName) {
        database.databaseWriteExecutor.execute(() -> movieListDao.deleteMovieList(movieListName));
    }
}
