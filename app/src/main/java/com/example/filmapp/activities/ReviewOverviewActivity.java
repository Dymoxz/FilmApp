package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.presentation.MovieReviewAdapter;
import com.example.filmapp.R;
import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.MovieReviewResponse;
import com.example.filmapp.model.MovieReview;
import com.example.filmapp.Activities.MainActivity;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewOverviewActivity extends AppCompatActivity {

    private static final String API_KEY = "02ddd233c99c814bad1a7d4af98e681b";
    private RecyclerView recyclerView;
    private MovieReviewAdapter adapter;
    private List<MovieReview> reviewList = new ArrayList<>();
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_overview_activity);
        Intent intent = getIntent();
        // Retrieving movieId from intent
        int movieId = intent.getIntExtra("MOVIE_ID", -1);
        Log.d("ReviewOverviewActivity", "Received movie ID: " + movieId);


        recyclerView = findViewById(R.id.reviewOverviewRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieReviewAdapter(reviewList);
        recyclerView.setAdapter(adapter);

        // Initialize Retrofit API interface
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);
        }

        // Start WriteReviewActivity through floating action button
        findViewById(R.id.fab).setOnClickListener(view ->
                startActivity(new Intent(this, WriteReviewActivity.class))
        );

        // Retrieve movie ID from intent MovieDetailActivity

        Log.d("ReviewOverviewActivity", "Received movie ID: " + movieId);
        if (movieId != -1) {
            Log.d("ReviewOverviewActivity", "Call api with received movie ID: " + movieId);
            // Fetch reviews for this movie ID and display them
            fetchMovieReviews(movieId);
        } else {
            Log.e("ReviewOverviewActivity", "Movie ID not received");
        }
    }

    // Navigates back to MainActivity if home button clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to fetch movie reviews
    private void fetchMovieReviews(int movieId) {
        Call<MovieReviewResponse> call = apiInterface.getReviews(278, API_KEY);
        call.enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(Call<MovieReviewResponse> call, Response<MovieReviewResponse> response) {
                if (response.isSuccessful()) {
                    MovieReviewResponse movieReviewResponse = response.body();
                    if (movieReviewResponse != null) {
                        // Clear the list before adding reviews
                        reviewList.clear();
                        reviewList.addAll(movieReviewResponse.getReviews());
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("API error", "Failed to fetch the movie reviews because response is null: " + response.message());
                    }
                }
            }


            @Override
            public void onFailure(Call<MovieReviewResponse> call, Throwable t) {
                Log.e("API error", "Failed to fetch the movie reviews: " + t.getMessage());
                t.printStackTrace(); // Print the stack trace
            }
        });
    }
}