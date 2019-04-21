package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.SerializedTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.SerializedTrips;
import com.fiuba.tdpii.correapp.models.web.Trip;
import com.fiuba.tdpii.correapp.models.web.TripSerialized;
import com.fiuba.tdpii.correapp.services.trips.TripAdapter;
import com.fiuba.tdpii.correapp.services.trips.TripClient;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoferActivity extends AppCompatActivity {


    private ArrayList<SerializedTripPostResponse> tripsArray;
    private Integer tripPosition;
    private TripService tripService;

    private ImageView backArrow;
    private Long driverId;
    private Bundle bundle;

    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CODE_ADDED_PILL = 400;

    public ChoferActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer_activity);


        bundle = getIntent().getParcelableExtra("bundle");

        driverId = bundle.getLong("driverId");

        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tripService = new TripService();
        setupInitials();

    }


    private void setupInitials() {

        tripsArray = new ArrayList<SerializedTripPostResponse>();

        tripService.getTrips().enqueue(new Callback<List<SerializedTripPostResponse>>() {
            @Override
            public void onResponse(Call<List<SerializedTripPostResponse>> call, Response<List<SerializedTripPostResponse>> response) {

                List<SerializedTripPostResponse> tripResponseArrayList = response.body();


                for (SerializedTripPostResponse trip : tripResponseArrayList) {
                    tripsArray.add(trip);
                }

                displayTrips();

            }

            @Override
            public void onFailure(Call<List<SerializedTripPostResponse>> call, Throwable t) {
                Toast.makeText(ChoferActivity.this, "FAILURE", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void displayTrips() {
        final ListView tripList = (ListView) findViewById(R.id.list_of_trips);
        TripAdapter tripAdapter = new TripAdapter(this, tripsArray);
        tripList.setAdapter(tripAdapter);
        tripList.setSelection(this.tripsArray.size());

        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                tripPosition = position;

                SerializedTripPostResponse trip = tripsArray.get(position);
                Toast.makeText(ChoferActivity.this, trip.getClient(), Toast.LENGTH_SHORT).show();

                Intent navigationIntent = new Intent(ChoferActivity.this, ChoferViewTripActivity.class);
                Bundle bundle = new Bundle();

                bundle.putLong("tripId", trip.getId());
                bundle.putLong("driverId",driverId );

                navigationIntent.putExtra("bundle", bundle);
                startActivity(navigationIntent);


                //    Intent intent = new Intent(PillboxActivity.this, DrinkedPillActivity.class);
                // intent.putExtra("pill", pill);
                // startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//            super.onActivityResult(requestCode, resultCode, data);
//
//            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//                TripSerialized trip = (TripSerialized) data.getSerializableExtra("trip");
//                tripsArray.set(tripPosition, trip);
//                displayTrips();
//
//            } else if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE_ADDED_PILL) {
//                ProgressBar loadingView = (ProgressBar) findViewById(R.id.loading);
//                loadingView.setVisibility(View.VISIBLE);
//                tripsArray = new ArrayList<TripSerialized>();
//                tripService.getTrips(this);
//            }
//        } catch (Exception ex) {
//            Toast.makeText(this, ex.toString(),
//                    Toast.LENGTH_SHORT).show();
//        }
//
//    }

    public ChoferActivity(ArrayList<SerializedTripPostResponse> tripsArray) {
        this.tripsArray = tripsArray;
    }

//    @Override
//    public void onResponseSuccess(Object responseBody) {
//        ArrayList<TripSerialized> tripResponseArrayList = (ArrayList<TripSerialized>) responseBody;
//
//        for (TripSerialized trip : tripResponseArrayList) {
//            tripsArray.add(trip);
//        }
//
//        ProgressBar loadingView = (ProgressBar) findViewById(R.id.loading);
//        loadingView.setVisibility(View.INVISIBLE);
//        displayTrips();
//    }
//
//    public void onResponseError() {
//        Toast.makeText(this, "Se produjo un error de conexión con la api, intente luego",
//                Toast.LENGTH_LONG).show();
//        ProgressBar loadingView = (ProgressBar) findViewById(R.id.loading);
//        loadingView.setVisibility(View.INVISIBLE);
//        finish();
//    }

}
