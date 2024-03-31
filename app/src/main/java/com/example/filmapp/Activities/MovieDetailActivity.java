package com.example.filmapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.filmapp.Application.RecyclerViewInterface;
import com.example.filmapp.R;
import com.example.filmapp.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity{

    private Movie movie;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);

        }

        TextView titleTextView = findViewById(R.id.movieDetailTitle);
        TextView genreTextView = findViewById(R.id.movieDetailGenre);
        TextView releaseYearTextView = findViewById(R.id.movieDetailReleaseYear);
        TextView durationTextView = findViewById(R.id.movieDetailDuration);
        TextView ratingTextView = findViewById(R.id.movieDetailRating);
        TextView taglineTextView = findViewById(R.id.movieDetailtagline);
        TextView descriptionTextView = findViewById(R.id.movieDetailDescription);
        ListView castListView = findViewById(R.id.movieDetailCastList);
        ImageView imageView = findViewById(R.id.movieDetailImage);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle !=null) {
            movie = (Movie) bundle.getSerializable("value");
            Log.d("DetailActivity", "got movie " + movie.getTitle());

        }
        if (movie != null) {
            StringBuilder genreIdStringBuilder = new StringBuilder();
            for (int genreId : movie.getGenreIdList()) {
                genreIdStringBuilder.append(genreId).append(", ");
            }
            genreTextView.setText(genreIdStringBuilder);
            titleTextView.setText(movie.getTitle());
            releaseYearTextView.setText(movie.getReleaseDate());
            durationTextView.setText(String.valueOf(movie.getDuration()));
            ratingTextView.setText(String.valueOf(movie.getRating()));
            taglineTextView.setText(movie.getTagline());
            descriptionTextView.setText(movie.getDescription());
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getImagePath()).into(imageView);
        }


        // Start review overview activity
        findViewById(R.id.openReviewOverviewButton).setOnClickListener(v ->
                startActivity(new Intent(MovieDetailActivity.this, ReviewOverviewActivity.class)));

    }


}
