package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fiuba.tdpii.correapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button client = findViewById(R.id.client);
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigationIntent = new Intent(MainActivity.this, MapHomeActivity.class);
                startActivity(navigationIntent);
                finish();
            }
        });

        Button chofer = findViewById(R.id.chofer);

    }
}
