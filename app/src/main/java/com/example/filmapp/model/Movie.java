package com.example.filmapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.gson.annotations.SerializedName;
@TypeConverters({IntegerListConverter.class,VideoConverter.class, MovieReviewListConverter.class})
@Entity(tableName = "Movie")
public class Movie implements Serializable {
    @PrimaryKey()
    private int id;
    private String title;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    private String imagePath;
    @SerializedName("genre_ids")
    private List<Integer> genreIdList;

    @SerializedName("overview")
    private String description;
    @SerializedName("runtime")
    private int duration;
    @SerializedName("vote_average")
    private float rating;
    @SerializedName("tagline")
    private String tagline;

    private Video trailer;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        // Check if the releaseDate is already in the desired format
        if (releaseDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return releaseDate;
        } else {
            // Define the formatter for the input string format
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Parse the string to LocalDate
            LocalDate date = LocalDate.parse(releaseDate, inputFormatter);

            // Format the LocalDate to the desired output format
            return date.format(outputFormatter);
        }
    }

    public List<Integer> getGenreIdList() {
        return genreIdList;
    }
    private List<MovieReview> reviews;

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public float getRating() {
        return rating;
    }

    public String getTagline() {
        return tagline;
    }

    public Video getTrailer() {
        return trailer;
    }

    public List<MovieReview> getReviews() {return reviews; }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setGenreIdList(List<Integer> genreIdList) {
        this.genreIdList = genreIdList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setTrailer(Video trailer) {
        this.trailer = trailer;
    }

    public void setReviews(List<MovieReview> reviews) {this.reviews = reviews; }
}
