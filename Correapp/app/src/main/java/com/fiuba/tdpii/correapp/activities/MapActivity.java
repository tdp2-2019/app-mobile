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
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final int LOCATION_REQUEST_CODE = 101;
    private static final int ZOOM = 13;
    public static final int CUSTOM_MARKER_WIDTH = 100;
    public static final int CUSTOM_MARKER_HEIGHT = 100;

    private LatLng originLocation = null;
    private LatLng destinynLocation = null;

    private FloatingActionButton cont;
    public static final String ORIGIN_LOCATION_KEY = "origin-location-key";

    private SearchView searchView;
    private SearchView destinySearchView;

    private GoogleMap mMap;
    public static final String DESTINATION_KEY = "destination-location-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        searchView = findViewById(R.id.search_origin);
        destinySearchView = findViewById(R.id.search_destiny);

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        if (bundle != null) {
            if (bundle.getParcelable("lc_origin") != null) {
                originLocation = bundle.getParcelable("lc_origin");
            }
            if (bundle.getParcelable("lc_dest") != null) {
                destinynLocation = bundle.getParcelable("lc_dest");
            }
            if (bundle.getString("add_origin") != null) {
                searchView.setQuery(bundle.getString("add_origin"), false);
            }
            if (bundle.getString("add_dest") != null) {
                searchView.setQuery(bundle.getString("add_dest"), false);
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fetchLastLocation();

        FloatingActionButton goBack = findViewById(R.id.back);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(MapActivity.this, MapHomeActivity.class);
                startActivity(backIntent);
                finish();
            }
        });

        FloatingActionButton currentLocationButton = findViewById(R.id.curr_location);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                originLocation = latLng;

                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(originLocation);
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_leash_dog, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.icon_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(originLocation.latitude + " : " + originLocation.longitude);

                // Clears the previously touched position
                mMap.clear();

                setDestinyMarker(mMap);

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                String originAddres = getAddress(originLocation);
                LatLngBounds.Builder bc = new LatLngBounds.Builder();

                bc.include(destinynLocation).include(originLocation);

                List<LatLng> path = getPath(originLocation, destinynLocation);
                path.stream().forEach(pathPoint -> {
                    bc.include(pathPoint);
                });

                //Draw the polyline
                addPoly(path, mMap);

                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                searchView.setQueryHint(originAddres);
                searchView.setQuery(originAddres, false);


            }
        });

        cont = findViewById(R.id.cont);
        cont.setVisibility(View.VISIBLE);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent destinyIntent = new Intent(MapActivity.this, CreateTripActivity.class);
                Bundle args = getIntent().getParcelableExtra("bundle");
                if (args == null) {
                    args = new Bundle();
                }
                args.putParcelable("lc_origin", originLocation);
                args.putParcelable("lc_dest", destinynLocation);

                args.putString("add_origin", searchView.getQuery().toString());
                args.putString("add_dest", destinySearchView.getQuery().toString());

                destinyIntent.putExtra("bundle", args);
                startActivity(destinyIntent);
            }
        });

        searchView.onActionViewExpanded();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.clearFocus();
            }
        }, 300);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                originLocation = getLocation(query);

                if(originLocation == null) {
                    Toast.makeText(MapActivity.this, "No se encontro localidad", Toast.LENGTH_SHORT).show();

                    return false;
                }
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(originLocation);
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_leash_dog, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.icon_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(originLocation.latitude + " : " + originLocation.longitude);

                // Clears the previously touched position
                mMap.clear();

                setDestinyMarker(mMap);

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                String originAddres = getAddress(originLocation);
                LatLngBounds.Builder bc = new LatLngBounds.Builder();

                bc.include(destinynLocation).include(originLocation);

                List<LatLng> path = getPath(originLocation, destinynLocation);
                path.stream().forEach(pathPoint -> {
                    bc.include(pathPoint);
                });

                //Draw the polyline
                addPoly(path, mMap);


                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                searchView.setQueryHint(originAddres);
                searchView.setQuery(originAddres, false);

                searchView.clearFocus();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        destinySearchView.onActionViewExpanded();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                destinySearchView.clearFocus();
            }
        }, 300);
        destinySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                destinynLocation = getLocation(query);
                if(destinynLocation == null) {
                    Toast.makeText(MapActivity.this, "No se encontro localidad", Toast.LENGTH_SHORT).show();

                    return false;
                }
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(destinynLocation);
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_leash_dog, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.icon_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(destinynLocation.latitude + " : " + destinynLocation.longitude);

                // Clears the previously touched position
                mMap.clear();

                setOriginMarker(mMap);
                // Animating to the touched position

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                String destinyAddres = getAddress(destinynLocation);
                LatLngBounds.Builder bc = new LatLngBounds.Builder();

                bc.include(destinynLocation).include(originLocation);

                List<LatLng> path = getPath(originLocation, destinynLocation);
                path.stream().forEach(pathPoint -> {
                    bc.include(pathPoint);
                });

                //Draw the polyline
                addPoly(path, mMap);


                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                destinySearchView.setQueryHint(destinyAddres);
                destinySearchView.setQuery(destinyAddres, false);

                destinySearchView.clearFocus();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        destinySearchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( originLocation == null || destinynLocation == null ){
                    Toast.makeText(MapActivity.this, "Te olvidaste de decir la ruta del viaje", Toast.LENGTH_SHORT).show();
                } else {
                    Intent destinyIntent = new Intent(MapActivity.this, CreateTripActivity.class);
                    Bundle args = getIntent().getParcelableExtra("bundle");
                    if (args == null) {
                        args = new Bundle();
                    }
                    args.putParcelable("lc_origin", originLocation);
                    args.putParcelable("lc_dest", destinynLocation);

                    args.putString("add_origin", getAddress(originLocation));
                    args.putString("add_dest", getAddress(destinynLocation));

                    destinyIntent.putExtra("bundle", args);
                    startActivity(destinyIntent);
                }
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
                    supportMapFragment.getMapAsync(MapActivity.this);
                } else {
                    Toast.makeText(MapActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        if (originLocation == null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            originLocation = latLng;
        }
        searchView.setQueryHint(getAddress(originLocation));

        if (destinynLocation == null) {
            Bundle bundle = getIntent().getParcelableExtra("bundle");
            destinynLocation = bundle.getParcelable("SearchQuery");
        }

        destinySearchView.setQueryHint(getAddress(destinynLocation));

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
                    Toast.makeText(MapActivity.this, "Location permission missing", Toast.LENGTH_SHORT).show();
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

}
