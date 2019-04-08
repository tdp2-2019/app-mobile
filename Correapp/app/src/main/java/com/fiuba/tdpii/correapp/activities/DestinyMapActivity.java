package com.fiuba.tdpii.correapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.fiuba.tdpii.correapp.activities.MapActivity.ORIGIN_LOCATION_KEY;

public class DestinyMapActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String DESTINY_LOCATION_KEY = "destiny-location";
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    public static final int ZOOM = 16;
    private static final int CUSTOM_MARKER_WIDTH = 100;
    private static final int CUSTOM_MARKER_HEIGHT = 100;

    private LatLng originLocation;
    private LatLng destinyLocation;
    private Button choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destiny_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(DestinyMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DestinyMapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        originLocation = bundle.getParcelable(ORIGIN_LOCATION_KEY);

        TextView origen = findViewById(R.id.origen);
        origen.setText(String.format("Origen: %s", originLocation.toString()));

        fetchLastLocation();

//        choose = findViewById(R.id.btn_choose);
        choose.setVisibility(View.GONE);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent destinyIntent = new Intent(DestinyMapActivity.this, ShowRouteActivity.class);
                Bundle args = new Bundle();
                args.putParcelable(ORIGIN_LOCATION_KEY,originLocation);
                args.putParcelable(DESTINY_LOCATION_KEY, destinyLocation);
                destinyIntent.putExtra("bundle", args );
                startActivity(destinyIntent);
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
                    Toast.makeText(DestinyMapActivity.this,currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(DestinyMapActivity.this);
                }else{
                    Toast.makeText(DestinyMapActivity.this,"No Location recorded",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOriginMarker(final GoogleMap googleMap){
        MarkerOptions markerOptions = new MarkerOptions().position(originLocation).title("You are Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, ZOOM));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));

        //Adding the created the marker on the map
        googleMap.addMarker(markerOptions);
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions

        setOriginMarker(googleMap);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_leash_dog, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_marker, CUSTOM_MARKER_WIDTH, CUSTOM_MARKER_HEIGHT)));

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                googleMap.clear();
                // Animating to the touched position


                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);

                setOriginMarker(googleMap);

                destinyLocation = latLng;

                LatLngBounds.Builder bc = new LatLngBounds.Builder();

                    bc.include(destinyLocation).include(originLocation);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));

                choose.setVisibility(View.VISIBLE);

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(DestinyMapActivity.this,"Location permission missing",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public Bitmap resizeMapIcons(Integer iconId, int width, int height){

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), iconId );
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}