package com.fiuba.tdpii.correapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;

import com.fiuba.tdpii.correapp.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class RateTripActivity extends AppCompatActivity {

    private SwitchMaterial switchMaterial;
    private LinearLayout commentLayout;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_trip);

        backArrow = (ImageView) findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateTripActivity.super.onBackPressed();
            }
        });
        commentLayout = (LinearLayout) findViewById(R.id.comment_layout);

        switchMaterial = (SwitchMaterial) findViewById(R.id.switch_material);
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    commentLayout.setVisibility(View.VISIBLE);
                } else {
                    commentLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
