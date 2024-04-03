package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.filmapp.R;
import com.example.filmapp.application.repository.MovieReviewRepository;
import com.example.filmapp.application.viewmodel.MovieReviewViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.MovieReview;

import java.time.LocalDate;
import java.util.UUID;

public class WriteReviewActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText reviewEditText;
    private MovieReviewViewModel movieReviewViewModel;
    private int movieId;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_write_review_activity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            movie = (Movie) bundle.getSerializable("value");
            if (movie!=null){
                movieId = movie.getId();
                Log.d("WriteReviewActivity", "recieverd movie id with id : " + movieId);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MovieReviewRepository movieReviewRepository = new MovieReviewRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieReviewDao());
        movieReviewViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieReviewViewModel.class);
        movieReviewViewModel.init(movieReviewRepository);

        firstNameEditText = findViewById(R.id.writeReviewFirstName);
        lastNameEditText = findViewById(R.id.writeReviewLastName);
        reviewEditText = findViewById(R.id.writeReviewEditText);

    }

    // Navigates back to MainActivity if home button clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("onOptionsItemSelected AAAAAAAAA", String.valueOf(id));

        if (id == android.R.id.home) {
            Log.d("onOptionsItemSelected", String.valueOf(id));
            // Create a new intent
            Intent intent = new Intent(this, ReviewOverviewActivity.class);

            // Update the movie object in the intent with the updated movie
            Bundle bundleReview = new Bundle();
            bundleReview.putSerializable("value", movie);
            intent.putExtras(bundleReview);

            // Start the ReviewOverviewActivity with the updated intent
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onSubmitButton(View view) {

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String review = reviewEditText.getText().toString().trim();
        String reviewId = UUID.randomUUID().toString();
        // Checks if a field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || review.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields before submitting", Toast.LENGTH_SHORT).show();
        } else {
           // Logic for adding a review to review section/database ??
            MovieReview movieReview = new MovieReview();
            movieReview.setReviewContent(review);
            movieReview.setDateCreated(LocalDate.now().toString());
            movieReview.setAuthor(firstName + " " + lastName);
            movieReview.setId(reviewId);
            movieReview.setMovieId(movieId);

            movieReviewViewModel.insertMovieReview(movieReview);
            Intent intent = new Intent(this, ReviewOverviewActivity.class);
            Bundle bundleReview = new Bundle();
            bundleReview.putSerializable("value", movie);
            intent.putExtras(bundleReview);
            startActivity(intent);
        }
    }
}
