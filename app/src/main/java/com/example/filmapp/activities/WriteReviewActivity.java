package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.filmapp.R;

public class WriteReviewActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText reviewEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_write_review_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);

        }

        firstNameEditText = findViewById(R.id.writeReviewFirstName);
        lastNameEditText = findViewById(R.id.writeReviewLastName);
        reviewEditText = findViewById(R.id.writeReviewEditText);

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

    public void onSubmitButton(View view) {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String review = reviewEditText.getText().toString().trim();

        // Checks if a field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || review.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields before submitting", Toast.LENGTH_SHORT).show();
        } else {
           // Logic for adding a review to review section/database ??
            Intent intent = new Intent(this, ReviewOverviewActivity.class);
            startActivity(intent);
        }
    }
}
