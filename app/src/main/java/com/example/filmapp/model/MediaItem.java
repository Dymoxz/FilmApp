package com.example.filmapp.model;

public class MediaItem {

    private String imagePath;

    private String videoUrl;

    public MediaItem(String imagePath, String videoUrl) {
        this.imagePath = imagePath;
        this.videoUrl = videoUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

}
