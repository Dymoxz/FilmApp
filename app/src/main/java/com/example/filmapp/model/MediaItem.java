package com.example.filmapp.model;

public class MediaItem {
    private String imagePath; // Added field to store image path
    private String videoUrl;

    public MediaItem(String imagePath, String videoUrl) {
        this.imagePath = imagePath; // Updated constructor to initialize imagePath
        this.videoUrl = videoUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
