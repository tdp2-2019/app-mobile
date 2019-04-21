package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.fiuba.tdpii.correapp.R;

public class TripTooManyRejectsActivity extends AppCompatActivity {

    private Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_too_many_rejects);

        retry = findViewById(R.id.create_trip);
        retry.setOnClickListener(v -> {

            Intent navigationIntent = new Intent(TripTooManyRejectsActivity.this, MapHomeActivity.class);
            startActivity(navigationIntent);

        });

    }
}
