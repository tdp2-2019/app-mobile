package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.PutTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTrip;
import com.fiuba.tdpii.correapp.models.web.Trip;
import com.fiuba.tdpii.correapp.models.web.TripPutRequest;
import com.fiuba.tdpii.correapp.services.trips.TripService;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoferViewTripActivity extends AppCompatActivity {

    private Long tripId;
    private Bundle bundle;
    private TripService tripService;

    private TextView nombre;
    private ImageView imagenPerfil;
    private TextView duracion;
    private TextView mascotas;
    private TextView reserva;
    private TextView destino;
    private TextView origen;

    private Button aceptar;
    private Button rechazar;

    private LatLng orLoc;
    private LatLng destLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer_view_trip);

        tripService = new TripService();

        nombre = findViewById(R.id.nombre);
        imagenPerfil = findViewById(R.id.imagen_perfil);
        duracion = findViewById(R.id.duracion);
        mascotas = findViewById(R.id.mascotas);
        reserva = findViewById(R.id.reserva);
        destino = findViewById(R.id.destino);
        origen = findViewById(R.id.origen);

        aceptar = findViewById(R.id.confirm);
        rechazar = findViewById(R.id.rechazar);

        bundle = getIntent().getParcelableExtra("bundle");

        tripId = bundle.getLong("id");

        tripService.getTripById(tripId.toString()).enqueue(new Callback<SerializedTrip>() {
            @Override
            public void onResponse(Call<SerializedTrip> call, Response<SerializedTrip> response) {

                SerializedTrip s_trip = response.body();
                Trip trip = s_trip.getTrip();

                nombre.setText(trip.getClient());
                duracion.setText(Integer.valueOf(Double.valueOf(trip.getDuration()/60).intValue()).toString() + " minutos");
                mascotas.setText(trip.getPets()!= null ? trip.getPets() : "mascotas null");

                Date startDate = new Date(trip.getStartTime());

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                String dateStr = "El " + calendar.get(Calendar.DAY_OF_MONTH) + " de " + theMonth(calendar.get(Calendar.MONTH));

                reserva.setText(dateStr);

                LatLng dest = new LatLng(Double.valueOf(trip.getDestination().getLat()), Double.valueOf(trip.getDestination().getLong() ));
                destLoc = dest;

                LatLng sourc = new LatLng(Double.valueOf(trip.getSource().getLat()), Double.valueOf(trip.getSource().getLong() ));
                orLoc = sourc;

                destino.setText(getAddress(dest));
                origen.setText(getAddress(sourc));

                System.out.print(response.body().getTrip().getClient());

            }

            @Override
            public void onFailure(Call<SerializedTrip> call, Throwable t) {

            }
        });

        rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent navigationIntent = new Intent(ChoferViewTripActivity.this, RechazarActivity.class);
                Bundle bundle = new Bundle();

                bundle.putLong("id", tripId);

                navigationIntent.putExtra("bundle", bundle);
                startActivity(navigationIntent);
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TripPutRequest putBody = new TripPutRequest();
                putBody.setDriverId("1");

                tripService.updateDriver(putBody, tripId.toString()).enqueue(new Callback<PutTrip>() {
                    @Override
                    public void onResponse(Call<PutTrip> call, Response<PutTrip> response) {

                        response.body();

                        bundle.putLong("id",tripId );

                        Intent navigationIntent = new Intent(ChoferViewTripActivity.this, ChoferSeguimiento.class);

                        navigationIntent.putExtra("bundle", bundle );
                        startActivity(navigationIntent);
                    }

                    @Override
                    public void onFailure(Call<PutTrip> call, Throwable t) {

                    }
                });


            }
        });


    }

    public static String theMonth(int month){
        String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return monthNames[month];
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


}
