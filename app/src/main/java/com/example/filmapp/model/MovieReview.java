package com.example.filmapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


import com.google.gson.annotations.SerializedName;

@Entity(tableName = "MovieReview")
public class MovieReview {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("author")
    private String author;

    @SerializedName("created_at")
    private String dateCreated;

    @SerializedName("content")
    private String reviewContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
