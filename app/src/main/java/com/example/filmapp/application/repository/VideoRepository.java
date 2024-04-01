package com.example.filmapp.application.repository;

import androidx.lifecycle.LiveData;

import com.example.filmapp.data.Database;
import com.example.filmapp.data.GenreDao;
import com.example.filmapp.data.VideoDao;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Video;

import java.util.List;

public class VideoRepository {

    private Database database;
    private VideoDao videoDao;
    private LiveData<List<Video>> listVideos;


    public VideoRepository(Database database, VideoDao videoDao) {
        this.database = database;
        this.videoDao = videoDao;
//        this.listVideos = videoDao.getAllVideos();

    }


    public void insertVideo(Video video) {
        database.databaseWriteExecutor.execute(() -> videoDao.insertVideo(video));
    }

    public LiveData<List<Video>> getAllVideos() {
        return listVideos;
    }
    public LiveData<Video> getVideo(int movieId) {
        return videoDao.getVideo(movieId);
    }

}

