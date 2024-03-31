package com.example.filmapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filmapp.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        TextView titleTextView = findViewById(R.id.movieDetailTitle);
        TextView genreTextView = findViewById(R.id.movieDetailGenre);
        TextView releaseYearTextView = findViewById(R.id.movieDetailReleaseYear);
        TextView durationTextView = findViewById(R.id.movieDetailDuration);
        TextView ratingTextView = findViewById(R.id.movieDetailRating);
        TextView taglineTextView = findViewById(R.id.movieDetailtagline);
        TextView descriptionTextView = findViewById(R.id.movieDetailDescription);
        ListView castListView = findViewById(R.id.movieDetailCastList);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle !=null) {
            movie = (Movie) bundle.getSerializable("value");
        }

    }
}
