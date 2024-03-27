package com.example.filmapp.Data;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.filmapp.model.Movie;

@androidx.room.Database(entities = {Movie.class}, version = 1,exportSchema = false )
public abstract class Database extends RoomDatabase {
    public abstract MovieDao movieDao();

    private static Database INSTANCE;

    public static Database getDatabaseInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "Movie").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
