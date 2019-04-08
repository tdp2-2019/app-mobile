package com.fiuba.tdpii.correapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fiuba.tdpii.correapp.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton rateTripDriverActivity;
    private MaterialButton rateTripClientActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rateTripDriverActivity = (MaterialButton) findViewById(R.id.activity_rate_trip_driver);
        rateTripDriverActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(getApplicationContext(), RateTripDriverActivity.class);
                startActivity(activity);
            }
        });
        rateTripClientActivity = (MaterialButton) findViewById(R.id.activity_rate_trip_client);
        rateTripClientActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(getApplicationContext(), RateTripClientActivity.class);
                startActivity(activity);
            }
        });
    }
}
