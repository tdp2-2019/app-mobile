package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.StartTripPutRequest;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripTooManyRejectsActivity extends AppCompatActivity {

    private Button retry;

    private TripService tripService;
    private Bundle bundle;
    private Long tripId;
    private Long clientId;
    private String client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_too_many_rejects);
        bundle = getIntent().getParcelableExtra("bundle");

        if (bundle != null) {
            tripId = bundle.getLong("tripId");
            clientId = bundle.getLong("clientId");
            client = bundle.getString("client");
        }



        tripService = new TripService();

        StartTripPutRequest request = new StartTripPutRequest();
        request.setStatus("finished");
        tripService.startTrip(request,tripId.toString()).enqueue(new Callback<SerializedTripPostResponse>() {
            @Override
            public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {

            }
        });

        retry = findViewById(R.id.create_trip);
        retry.setOnClickListener(v -> {



            Intent navigationIntent = new Intent(TripTooManyRejectsActivity.this, MapHomeActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("client",client );
            bundle.putLong("clientId", clientId );
            navigationIntent.putExtra("bundle",bundle );
            startActivity(navigationIntent);

        });

    }
}
