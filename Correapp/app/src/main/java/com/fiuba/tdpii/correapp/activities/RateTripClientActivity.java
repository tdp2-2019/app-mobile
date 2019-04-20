package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;

public class RateTripClientActivity extends AppCompatActivity {

    public final static String TRIP_ID_KEY = "trip_id_key";

    private Switch switchMaterial;
    private Switch switchMaterialApp;
    private Switch switchMaterialCar;
    private Button buttonSubmit;
    private RatingBar ratingBar;

    private LinearLayout commentLayout;
    private EditText textInputComment;

    private int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_trip_driver);

        Intent intent = getIntent();
        tripId = intent.getIntExtra(TRIP_ID_KEY, 0);

//        if (tripId == 0) {
//            showErrorMessage();
//            // finish();
//            // return;
//        }

        ratingBar = findViewById(R.id.rating_bar);
        commentLayout = findViewById(R.id.comment_layout);
        textInputComment = findViewById(R.id.comment);

        switchMaterial = findViewById(R.id.switch_material);
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    commentLayout.setVisibility(View.VISIBLE);
                } else {
                    commentLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: implementar puntuado de viaje para chofer. Falta servicio de API.
                int rating = ratingBar.getNumStars();
                boolean isClientGood = !switchMaterial.isChecked();
                String comment = textInputComment.getText().toString();

                Intent mainActivity = new Intent(getApplicationContext(), ChoferActivity.class);
                startActivity(mainActivity);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(TRIP_ID_KEY, tripId);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        tripId = savedInstanceState.getInt(TRIP_ID_KEY);
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Se produjo un error inesperado, intente nuevamente", Toast.LENGTH_LONG).show();
    }
}