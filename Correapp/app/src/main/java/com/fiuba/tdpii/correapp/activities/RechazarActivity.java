package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Rejection;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.TripRejectionRequest;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechazarActivity extends AppCompatActivity {

    private Button enviar;
    private ImageView backArrow;
    private Long tripId;
    private Long driverId;

    private EditText comments;

    private Bundle bundle;

    private TripService tripService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechazar);


        tripService = new TripService();

        bundle = getIntent().getParcelableExtra("bundle");

        tripId = bundle.getLong("tripId");
        driverId = bundle.getLong("driverId");

        comments = findViewById(R.id.obs);


        enviar = findViewById(R.id.enviar);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TripRejectionRequest request = new TripRejectionRequest();
                Rejection rejection = new Rejection();
                rejection.setComment(comments.getText().toString());
                rejection.setDriverId(driverId);

                request.setRejection(rejection);
                tripService.rejectTrip(request,tripId.toString() ).enqueue(new Callback<SerializedTripPostResponse>() {
                    @Override
                    public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {

                        response.body();

                        bundle.putLong("driverId",driverId );


                        Intent navigationIntent = new Intent(RechazarActivity.this, DriverProfileActivity.class);
                        navigationIntent.putExtra("bundle", bundle );

                        startActivity(navigationIntent);
                    }

                    @Override
                    public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {

                    }
                });;


            }
        });

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
