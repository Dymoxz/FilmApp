package com.example.filmapp.Application;

import androidx.lifecycle.LiveData;

import com.example.filmapp.Data.Database;
import com.example.filmapp.Data.MovieDao;
import com.example.filmapp.Data.MovieListDao;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.MovieList;

import java.util.List;

public class MovieListRepository {
    private Database database;

    private MovieListDao movieListDao;

    private LiveData<List<MovieList>> listMoviesList;
    public MovieListRepository(Database database, MovieListDao movieListDao) {
        this.database = database;
        this.movieListDao = movieListDao;
        this.listMoviesList = movieListDao.getAllMovieLists();
    }


    public LiveData<List<MovieList>> getAllMovieLists() { return listMoviesList; }

    void insertMovieList(MovieList movieList) {
        database.databaseWriteExecutor.execute(() -> movieListDao.insertMovieList(movieList));
    }
}
