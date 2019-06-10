package com.fiuba.tdpii.correapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.fiuba.tdpii.correapp.R;

public class ClientBlockedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_blocked);


        Bundle bundle = getIntent().getParcelableExtra("bundle");

        Long driverId = bundle.getLong("clientId");

        String comment = bundle.getString("comment");


        TextView commentView = findViewById(R.id.subtexto);
        commentView.setText(comment);


    }

    @Override
    public void onBackPressed(){

    }
}
