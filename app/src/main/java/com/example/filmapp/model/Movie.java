package com.example.filmapp.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.gson.annotations.SerializedName;


public class Movie implements Serializable {
    private int id;
    private String title;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    private String imagePath;
    @SerializedName("genre_ids")
    private List<Integer> genreIdList;
    private List<Genre> genreList;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        // Define the formatter for the input string format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


        // Parse the string to LocalDate
        LocalDate date = LocalDate.parse(releaseDate, inputFormatter);
        return date.format(outputFormatter);
    }

    public List<Integer> getGenreIdList() {
        return genreIdList;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }
}
