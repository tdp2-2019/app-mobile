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
import com.fiuba.tdpii.correapp.models.web.DriversByTrip;
import com.fiuba.tdpii.correapp.models.web.Rejected;
import com.fiuba.tdpii.correapp.models.web.SerializedTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.SerializedTrips;
import com.fiuba.tdpii.correapp.models.web.Trip;
import com.fiuba.tdpii.correapp.models.web.TripSerialized;
import com.fiuba.tdpii.correapp.services.trips.TripAdapter;
import com.fiuba.tdpii.correapp.services.trips.TripClient;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Boolean fetched = Boolean.FALSE;

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
                    if (trip.getStatus() != null && trip.getStatus().equals("created") && trip.getDriverId() == null) {
                        tripService.getDriversByTrip(trip.getId().toString()).enqueue(new Callback<List<DriversByTrip>>() {
                            @Override
                            public void onResponse(Call<List<DriversByTrip>> call, Response<List<DriversByTrip>> response) {
                                List<DriversByTrip> drivers = response.body();
                                tripService.getRejectedsByTrip(trip.getId().toString()).enqueue(new Callback<List<Rejected>>() {

                                    @Override
                                    public void onResponse(Call<List<Rejected>> call, Response<List<Rejected>> response) {

                                        List<Rejected> rejections = response.body();

                                        if (response.code() == 404) {

                                            if (drivers.get(0).getDriverId().equals(driverId)) {
                                                tripsArray.add(trip);
                                                fetched = Boolean.TRUE;
                                            }

                                        } else {

                                            Map<Long, Long> ids = getRejectedDriverIds(rejections);
                                            Integer index = 0;
                                            while (Boolean.TRUE) {
                                                DriversByTrip bestDriver = drivers.get(index);
                                                if(bestDriver.getRing() >= 11){
                                                    break;
                                                }
                                                if (bestDriver.getDriverId().equals(driverId)) {
                                                    if (!ids.containsKey(driverId)) {
                                                        tripsArray.add(trip);
                                                        fetched = Boolean.TRUE;
                                                        break;
                                                    } else{
                                                        //is rejected
                                                        break;
                                                    }
                                                } else {
                                                    if (!ids.containsKey(bestDriver.getDriverId())) {
                                                        break;
                                                    } else {
                                                        index++;
                                                    }
                                                }
                                            }
                                        }
                                        displayTrips();
                                    }

                                    @Override
                                    public void onFailure(Call<List<Rejected>> call, Throwable t) {
                                        System.out.print("see");
                                    }
                                });
                            }
                            @Override
                            public void onFailure(Call<List<DriversByTrip>> call, Throwable t) {
                                System.out.print("see");

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SerializedTripPostResponse>> call, Throwable t) {
                Toast.makeText(ChoferActivity.this, "No hay viajes asignados", Toast.LENGTH_SHORT).show();

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
        if(fetched) {
            final ListView tripList = (ListView) findViewById(R.id.list_of_trips);
            TripAdapter tripAdapter = new TripAdapter(this, tripsArray);
            tripList.setAdapter(tripAdapter);
            tripList.setSelection(this.tripsArray.size());

            tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                    tripPosition = position;

                    SerializedTripPostResponse trip = tripsArray.get(position);

                    Intent navigationIntent = new Intent(ChoferActivity.this, ChoferViewTripActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putLong("tripId", trip.getId());
                    bundle.putLong("driverId", driverId);

                    navigationIntent.putExtra("bundle", bundle);
                    startActivity(navigationIntent);

                }
            });
        }
    }


    public ChoferActivity(ArrayList<SerializedTripPostResponse> tripsArray) {
        this.tripsArray = tripsArray;
    }

}
