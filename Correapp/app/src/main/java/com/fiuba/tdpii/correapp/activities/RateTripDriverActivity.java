package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Rating;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.TripDriverRatingRequest;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateTripDriverActivity extends AppCompatActivity {


    private Switch switchMaterialDriver;
    private Switch switchMaterialApp;
    private Switch switchMaterialCar;
    private Button buttonSubmit;
    private RatingBar ratingBar;
    private EditText textInputComment;

    private Bundle bundle;
    private Long tripId;
    private Long driverId;
    private Long clientId;
    private String client;

    private TripService tripService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_trip);

        bundle = getIntent().getParcelableExtra("bundle");

        tripId = bundle.getLong("tripId");
        driverId = bundle.getLong("driverId");

        clientId = bundle.getLong("clientId");
        client = bundle.getString("client");

        tripService = new TripService();

        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        switchMaterialDriver = findViewById(R.id.switch_material_driver);
        switchMaterialApp =  findViewById(R.id.switch_material_app);
        switchMaterialCar =  findViewById(R.id.switch_material_car);
        textInputComment = findViewById(R.id.comment);

        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float ratingValue =  ratingBar.getRating();
                boolean improveDriver = switchMaterialDriver.isChecked();
                boolean improveApp= switchMaterialApp.isChecked();
                boolean improveCar = switchMaterialCar.isChecked();
                String comment = textInputComment.getText().toString();

                TripDriverRatingRequest request = new TripDriverRatingRequest();
                Rating rating = new Rating();
                rating.setRating(ratingValue.longValue());
                rating.setComment(comment);
                request.setRating(rating);
                tripService.rateDriver(request, tripId.toString()).enqueue(new Callback<SerializedTripPostResponse>() {
                    @Override
                    public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {
                        tripService.rateDriver(request, tripId.toString()).enqueue(new Callback<SerializedTripPostResponse>() {
                            @Override
                            public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {

                                Intent mainActivity = new Intent(getApplicationContext(), MapHomeActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putString("client", client);
                                bundle.putLong("clientId",clientId );

                                mainActivity.putExtra("bundle",bundle );
                                startActivity(mainActivity);
                            }

                            @Override
                            public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {

                    }
                });


            }
        });
    }


    private void showErrorMessage() {
        Toast.makeText(this, "Se produjo un error inesperado, intente nuevamente", Toast.LENGTH_LONG).show();
    }
}
