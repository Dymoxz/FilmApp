package com.example.filmapp.api.response;

import com.example.filmapp.model.Video;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {
    @SerializedName("id")
    private int movieId;
    @SerializedName("results")
    private List<Video> videos;

    public Video getTrailer() {
        if (videos != null) {
            for (Video video : videos) {
                if (video.isOfficial() && video.getType().equalsIgnoreCase("Trailer")) {
                    return video;
                }
            }
        }
        return null;
    }
}