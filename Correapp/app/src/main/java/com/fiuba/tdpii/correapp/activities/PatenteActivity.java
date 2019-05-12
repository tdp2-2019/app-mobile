package com.fiuba.tdpii.correapp.activities;

import android.media.Image;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fiuba.tdpii.correapp.R;

public class PatenteActivity extends AppCompatActivity {

    ImageView patente;
    String uri;
    private Bundle bundle;

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patente);


        bundle = getIntent().getParcelableExtra("bundle");

        uri = bundle.getString("patente");

        patente = findViewById(R.id.imageView);

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (uri != null)
            Glide.with(this).load(uri).into(patente);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
