package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Rating;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.TripClientRatingRequest;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateTripClientActivity extends AppCompatActivity {

    public final static String TRIP_ID_KEY = "trip_id_key";

    private Switch switchMaterial;
    private Switch switchMaterialApp;
    private Switch switchMaterialCar;
    private Button buttonSubmit;
    private RatingBar ratingBar;

    private LinearLayout commentLayout;
    private EditText textInputComment;

    private Bundle bundle;
    private Long tripId;
    private Long driverId;

    private TripService tripService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rate_trip_driver);

        tripService = new TripService();

        bundle = getIntent().getParcelableExtra("bundle");

        tripId = bundle.getLong("tripId");
        driverId = bundle.getLong("driverId");

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

                Float ratingValue = ratingBar.getRating();
                boolean isClientGood = !switchMaterial.isChecked();
                String comment = textInputComment.getText().toString();

                TripClientRatingRequest request = new TripClientRatingRequest();
                Rating rating = new Rating();
                rating.setRating(ratingValue.longValue());
                rating.setComment(comment);
                request.setRating(rating);
                tripService.rateClient(request, tripId.toString()).enqueue(new Callback<SerializedTripPostResponse>() {
                    @Override
                    public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("driverId",driverId );
                        Intent navigationIntent = new Intent(RateTripClientActivity.this, DriverProfileActivity.class);
                        navigationIntent.putExtra("bundle", bundle );
                        startActivity(navigationIntent);
                    }

                    @Override
                    public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(TRIP_ID_KEY, tripId);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        tripId = savedInstanceState.getLong(TRIP_ID_KEY);
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Se produjo un error inesperado, intente nuevamente", Toast.LENGTH_LONG).show();
    }

}
