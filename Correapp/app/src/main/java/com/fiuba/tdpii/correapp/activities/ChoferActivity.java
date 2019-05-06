package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Rejected;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.services.trips.TripAdapter;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoferActivity extends AppCompatActivity {


    private List<SerializedTripPostResponse> tripsArray;
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

        tripService.getTripsDoneByDriver(this.driverId.toString()).enqueue(new Callback<List<SerializedTripPostResponse>>() {
            @Override
            public void onResponse(Call<List<SerializedTripPostResponse>> call, Response<List<SerializedTripPostResponse>> response) {

                List<SerializedTripPostResponse> tripResponseArrayList = response.body();
                tripsArray = tripResponseArrayList;
                displayTrips();
            }

            @Override
            public void onFailure(Call<List<SerializedTripPostResponse>> call, Throwable t) {

            }
        });


    }


    private Map<Long, Long> getRejectedDriverIds(List<Rejected> rejections) {

        Map<Long, Long> response = new HashMap<>();

        for (Rejected rejection : rejections) {
            response.put(rejection.getDriverId(), rejection.getTripId());
        }
        return response;
    }

    private void displayTrips() {
        final ListView tripList = (ListView) findViewById(R.id.list_of_trips);
        TripAdapter tripAdapter = new TripAdapter(this, tripsArray);
        tripList.setAdapter(tripAdapter);
        if(this.tripsArray != null) {
            tripList.setSelection(this.tripsArray.size());
        }
        Double totalGains = getSumPrices(this.tripsArray);

        TextView total = findViewById(R.id.totalViajes);
        total.setText("Total recaudado: $ ".concat(String.format(Locale.ITALY, "%,d", totalGains.intValue())));

        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                tripPosition = position;

                SerializedTripPostResponse trip = tripsArray.get(position);

                Intent navigationIntent = new Intent(ChoferActivity.this, ChoferViewFinishedTripActivity.class);
                Bundle bundle = new Bundle();

                bundle.putLong("tripId", trip.getId());
                bundle.putLong("driverId", driverId);

                navigationIntent.putExtra("bundle", bundle);
                startActivity(navigationIntent);

            }
        });
    }

    private Double getSumPrices(List<SerializedTripPostResponse> tripsArray) {

        Double sum = 0d;

        for (SerializedTripPostResponse trip : tripsArray) {
            sum += trip.getPrice();
        }
        return sum;

    }


    public ChoferActivity(ArrayList<SerializedTripPostResponse> tripsArray) {
        this.tripsArray = tripsArray;
    }

}
