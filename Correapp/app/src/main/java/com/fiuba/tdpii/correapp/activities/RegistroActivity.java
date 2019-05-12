package com.fiuba.tdpii.correapp.activities;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fiuba.tdpii.correapp.R;

public class RegistroActivity extends AppCompatActivity {


    ImageView registro;
    String uri;
    private Bundle bundle;

    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        bundle = getIntent().getParcelableExtra("bundle");

        uri = bundle.getString("registro");

        registro = findViewById(R.id.imageView);

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (uri != null)
            Glide.with(this).load(uri).into(registro);

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
