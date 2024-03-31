package com.example.filmapp.Data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.filmapp.model.Genre;
import com.example.filmapp.model.MovieList;
import com.example.filmapp.model.Movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {Movie.class, MovieList.class, Genre.class}, version = 3,exportSchema = false )
public abstract class Database extends RoomDatabase {
    public abstract MovieDao movieDao();
    public abstract MovieListDao movieListDao();
    public abstract GenreDao genreDao();
    private static final int NUMBER_OF_THREADS = 4;
    private static Database INSTANCE;

    public static Database getDatabaseInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "Movie")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            ;
        }
        return INSTANCE;
    }

    public final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

}
