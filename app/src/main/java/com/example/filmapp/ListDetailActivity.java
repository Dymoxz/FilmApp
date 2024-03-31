package com.example.filmapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ListDetailActivity extends AppCompatActivity {
    String listName;
    String dateCreated;

    TextView listTextView;
    TextView dateTextView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        listTextView = findViewById(R.id.listTitle);
        dateTextView = findViewById(R.id.listDate);

        Intent intent = getIntent();
        listName = intent.getStringExtra("listName");
        dateCreated = intent.getStringExtra("listDate");
        Log.v("ListDetailActivity", "List Name: " + listName);
        Log.v("ListDetailActivity", "Date Created: " + dateCreated);



        // Set text to TextViews
        if (listName != null) {
            listTextView.setText(listName);
        }
        if (dateCreated != null) {
            dateTextView.setText(dateCreated);
        }
    }
}
