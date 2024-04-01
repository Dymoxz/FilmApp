package com.example.filmapp.application.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.filmapp.application.repository.GenreRepository;
import com.example.filmapp.application.repository.VideoRepository;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.Video;

import java.util.List;

public class VideoViewModel extends ViewModel {
    private VideoRepository repository;
    private LiveData<List<Video>> listVideos;

    private LiveData<String> videoKey;

    public void init(VideoRepository repository) {
        if (this.repository != null) {
            // ViewModel is already initialized
            return;
        }
        this.repository = repository;
        this.listVideos = repository.getAllVideos();
    }

    public LiveData<List<Video>> getListVideos() {
        return listVideos;
    }
    public LiveData<Video> getVideo(int movieId) {
        return repository.getVideo(movieId);
    }
    public void insertVideo(Video video) {
        repository.insertVideo(video);
    }


}
