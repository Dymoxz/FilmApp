package com.example.filmapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.filmapp.R;

public class ReviewOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_overview_activity);







        // Start WriteReviewActivity through floating action button
        findViewById(R.id.fab).setOnClickListener(view ->
                startActivity(new Intent(ReviewOverviewActivity.this, WriteReviewActivity.class))
        );
    }
}
