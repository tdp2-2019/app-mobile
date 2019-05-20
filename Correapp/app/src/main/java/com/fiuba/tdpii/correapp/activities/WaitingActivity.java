package com.fiuba.tdpii.correapp.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Rejected;
import com.fiuba.tdpii.correapp.models.web.SerializedTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.services.trips.TripService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingActivity extends FragmentActivity implements OnMapReadyCallback {
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final int LOCATION_REQUEST_CODE = 101;
    private static final int ZOOM = 16;
    public static final int CUSTOM_MARKER_WIDTH = 100;
    public static final int CUSTOM_MARKER_HEIGHT = 100;

    private LatLng originLocation;
    private LatLng destinynLocation;


    private GoogleMap mMap;

    private Bundle bundle;

    private TripService tripService;
    private Long tripId;
    private Long clientId;
    private String client;

    private ImageView backArrow;
    private CountDownTimer timer = new CountDownTimer(30000000, 3000) {

        @Override
        public void onTick(long millisUntilFinished) {
            yourMethod();
        }

        @Override
        public void onFinish() {
            try {

            } catch (Exception e) {
                Log.e("Error", "Error: " + e.toString());
            }
        }
    }.start();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        tripService = new TripService();



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(WaitingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WaitingActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fetchLastLocation();


        bundle = getIntent().getParcelableExtra("bundle");
        if (bundle != null) {
            originLocation = bundle.getParcelable("lc_origin");
            destinynLocation = bundle.getParcelable("lc_dest");
            tripId = bundle.getLong("id");
            clientId = bundle.getLong("clientId");
            client = bundle.getString("client");

        }


        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(WaitingActivity.this);
                } else {
                    Toast.makeText(WaitingActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void yourMethod() {
        if (tripId == null)
            return;
//        Toast.makeText(WaitingActivity.this, "Hago un get1", Toast.LENGTH_SHORT).show();
        tripService.getTripById(tripId.toString()).
                enqueue(new Callback<SerializedTripPostResponse>() {
                    @Override
                    public void onResponse
                            (Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {

                        response.body();
//                        Toast.makeText(WaitingActivity.this, "Hago un gets2", Toast.LENGTH_SHORT).show();


                        tripService.getRejectedsByTrip(tripId.toString()).enqueue(new Callback<List<Rejected>>() {
                            @Override
                            public void onResponse(Call<List<Rejected>> call, Response<List<Rejected>> response) {
                                List<Rejected> rejections = response.body();

                                if (response.code() == 404) {
                                    System.out.print("he");
                                } else {


                                    if (response.body().size() >= 3) {
                                        Intent navigationIntent = new Intent(WaitingActivity.this, TripTooManyRejectsActivity.class);
                                        timer.cancel();

                                        Bundle bundle = new Bundle();


                                        bundle.putLong("tripId", tripId);
                                        bundle.putLong("clientId",clientId );
                                        bundle.putString("client",client );

                                        navigationIntent.putExtra("bundle", bundle);

                                        startActivity(navigationIntent);
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<List<Rejected>> call, Throwable t) {

                            }
                        });

                        if (response.body().getDriverId() != null && (response.body().getStatus().equals("accepted") || response.body().getStatus().equals("started"))) {

//                            Intent navigationIntent = new Intent(WaitingActivity.this, SeguimientoActivity.class);

                            Intent navigationIntent = new Intent(WaitingActivity.this, ClientDriverProfileActivity.class);

                            Bundle bundle = new Bundle();

                            if (originLocation != null)
                                bundle.putParcelable("lc_origin", originLocation);
                            if (destinynLocation != null)
                                bundle.putParcelable("lc_dest", destinynLocation);
                            bundle.putLong("tripId", tripId);
                            bundle.putLong("driverId", response.body().getDriverId());

                            bundle.putLong("clientId",clientId );
                            bundle.putString("client",client );

                            navigationIntent.putExtra("bundle", bundle);
                            timer.cancel();
                            startActivity(navigationIntent);
                        }
                    }

                    @Override
                    public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;


        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions().position(originLocation).title("Origen");

        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

        //Adding the created the marker on the map
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.icon_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));
        googleMap.addMarker(markerOptions);

        MarkerOptions destinyMarkerOptions = new MarkerOptions().position(destinynLocation).title("Destino");

        //Adding the created the marker on the map
        destinyMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.icon_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));
        googleMap.addMarker(destinyMarkerOptions);


        LatLngBounds.Builder bc = new LatLngBounds.Builder();


        List<LatLng> path = getPath(originLocation, destinynLocation);
        path.stream().forEach(pathPoint -> {
            bc.include(pathPoint);
        });
        bc.include(destinynLocation).include(originLocation);
        addPoly(path, mMap);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 40));

//        Handler handler = new Handler();
//        Runnable runnableCode = new Runnable() {
//            @Override
//            public void run() {
//                System.out.print("HOLA");
//                tripService.getTripById(tripId.toString()).enqueue(new Callback<SerializedTripPostResponse>() {
//                    @Override
//                    public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {
//
//                        response.body();
//
//                        if (response.body().getDriverId() != null) {
//
////                            Intent navigationIntent = new Intent(WaitingActivity.this, SeguimientoActivity.class);
//
//                            Intent navigationIntent = new Intent(WaitingActivity.this, ClientDriverProfileActivity.class);
//
//                            Bundle bundle = new Bundle();
//
//                            if (originLocation != null)
//                                bundle.putParcelable("lc_origin", originLocation);
//                            if (destinynLocation != null)
//                                bundle.putParcelable("lc_dest", destinynLocation);
//                            bundle.putLong("tripId", tripId);
//                            bundle.putLong("driverId", response.body().getDriverId());
//
//                            navigationIntent.putExtra("bundle", bundle);
//                            startActivity(navigationIntent);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {
//
//                    }
//                });
//
//                // Do something here on the main thread
//                Log.d("Handlers", "Called on main thread");
//            }
//        };
//
//        handler.postDelayed(runnableCode, 2000);

    }


    public void addPoly(List<LatLng> path, GoogleMap mMap) {
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.parseColor("#6892E9")).width(10).jointType(JointType.ROUND);
            mMap.addPolyline(opts);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(WaitingActivity.this, "Location permission missing", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public Bitmap resizeMapIcons(Integer iconId, int width, int height) {

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), iconId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private void setOriginMarker(final GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions().position(originLocation).title("Origen");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.icon_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));

        //Adding the created the marker on the map
        googleMap.addMarker(markerOptions);
    }

    private void setDestinyMarker(final GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions().position(destinynLocation).title("Destino");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.icon_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));

        //Adding the created the marker on the map
        googleMap.addMarker(markerOptions);
    }

    private String getAddress(LatLng latLng) {
        // 1
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        Address address = null;
        try {
            // 2
            double longitude = latLng.longitude;
            double latitude = latLng.latitude;
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses.get(0);
            }
        } catch (IOException e) {
            Log.e("MapsActivity", e.getMessage());

        }

        return address != null ? address.getAddressLine(0) : "";
    }


    public LatLng getLocation(String search) {
        // 1
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        Address address = null;
        try {
            addresses = geocoder.getFromLocationName(search, 1, -34.6879526, -58.5990012,
                    -34.528286, -58.346597);
            // 2
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses.get(0);
            }
        } catch (IOException e) {
            Log.e("MapsActivity", e.getMessage());

        }

        return address != null ? new LatLng(address.getLatitude(), address.getLongitude()) : null;
    }

    private String getStringFromLatLng(LatLng loc) {
        return String.valueOf(loc.latitude) + "," + String.valueOf(loc.longitude);
    }

    private List<LatLng> getPath(LatLng origin, LatLng destiny) {

        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_api_key))
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, getStringFromLatLng(origin), getStringFromLatLng(destiny));
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {

        }
        return path;
    }


    @Override
    public void onBackPressed() {

    }

}
