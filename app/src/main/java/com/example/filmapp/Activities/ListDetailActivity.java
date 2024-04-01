package com.example.filmapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.filmapp.R;

public class ListDetailActivity extends AppCompatActivity {
    String listName;

    TextView listTextView;
    TextView dateTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        listTextView = findViewById(R.id.listTitle);

        Intent intent = getIntent();
        try {
            listName = intent.getStringExtra("listName");
            Log.v("ListDetailActivity", "List Name: " + listName);
        } catch (Exception e){
            Log.v("ERROR", "no list found" + e);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set text to TextViews
        if (listName != null) {
            listTextView.setText(listName);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_detail, menu);
        return true;
    }
}
