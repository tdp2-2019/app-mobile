package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Pet;
import com.fiuba.tdpii.correapp.models.web.Rating;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.services.trips.TripService;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoferViewFinishedTripActivity extends AppCompatActivity {

    private Long tripId;
    private Long driverId;

    private Bundle bundle;
    private TripService tripService;

    private TextView nombre;
    private TextView duracion;
    private TextView mascotas;
    private TextView mascotasDetalle;
    private TextView mascotasNombres;

    private TextView reserva;
    private TextView destino;
    private TextView origen;

    private TextView precio;

    private RatingBar rating;
    private TextView comentariosDelViaje;

    private ImageView goBack;

    private LatLng orLoc;
    private LatLng destLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer_view_finished_trip);

        tripService = new TripService();

        nombre = findViewById(R.id.nombre);
        duracion = findViewById(R.id.duracion);
        mascotas = findViewById(R.id.mascotas);
        reserva = findViewById(R.id.reserva);
        destino = findViewById(R.id.destino);
        origen = findViewById(R.id.origen);

        mascotasDetalle = findViewById(R.id.mascotas_detalle_1_descripcion);
        mascotasNombres = findViewById(R.id.mascotas_detalle_1_nombre);

        rating = findViewById(R.id.rating_bar);
        comentariosDelViaje = findViewById(R.id.rate_comentario);
        precio = findViewById(R.id.precio);


        goBack = findViewById(R.id.back_arrow);

        bundle = getIntent().getParcelableExtra("bundle");

        tripId = bundle.getLong("tripId");
        driverId = bundle.getLong("driverId");


        tripService.getTripById(tripId.toString()).enqueue(new Callback<SerializedTripPostResponse>() {
            @Override
            public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {

                SerializedTripPostResponse trip = response.body();

                nombre.setText(trip.getClient());



                Long duracionTrip = trip.getDuration();
                Long min = duracionTrip / 60;
                if(min < 60){
                    duracion.setText(min + " minutos");
                } else {

                    Long hs = min / 60;
                    min = min % 60;
                    duracion.setText(hs.toString() + ":" + min.toString() + " hs");
                }


                if (trip.getPets().size() == 1){
                    mascotas.setText("1 mascota");
                } else {
                    mascotas.setText(trip.getPets().size()  + " mascotas");
                }

                int count = 1;
                for(Pet pet : trip.getPets()){
                    switch (count) {
                        case 1:
                            mascotasDetalle.setText(pet.getKey2());
                            mascotasNombres.setText("Responde al nombre de " + pet.getKey1());
                            break;
                        case 2:
                            TextView detalle2 = findViewById(R.id.mascotas_detalle_2_descripcion);
                            detalle2.setText(pet.getKey2());
                            detalle2.setVisibility(View.VISIBLE);
                            TextView nombres2 = findViewById(R.id.mascotas_detalle_2_nombre);
                            nombres2.setText("Responde al nombre de " + pet.getKey1());
                            nombres2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            TextView detalle3 = findViewById(R.id.mascotas_detalle_3_descripcion);
                            detalle3.setText(pet.getKey2());
                            detalle3.setVisibility(View.VISIBLE);
                            TextView nombres3 = findViewById(R.id.mascotas_detalle_3_nombre);
                            nombres3.setText("Responde al nombre de " + pet.getKey1());
                            nombres3.setVisibility(View.VISIBLE);
                            break;
                    }
                    count++;
                }

                try {


                    String sDate1=trip.getStartTime().substring(0,10);
                    Date startDate= null;
                    try {
                        //2019-04-22T00:46:50.000Z
                        startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    String dateStr = "El " + calendar.get(Calendar.DAY_OF_MONTH) + " de " + theMonth(calendar.get(Calendar.MONTH));

                    reserva.setText(dateStr);
                } catch (Exception e){
                    reserva.setText("El día de hoy");
                    LinearLayout reservaLayout = findViewById(R.id.reserva_layout);
                    reservaLayout.setVisibility(View.GONE);
                }
                LatLng dest = new LatLng(Double.valueOf(trip.getDestination().getLat()), Double.valueOf(trip.getDestination().getLong() ));
                destLoc = dest;

                LatLng sourc = new LatLng(Double.valueOf(trip.getSource().getLat()), Double.valueOf(trip.getSource().getLong() ));
                orLoc = sourc;

                destino.setText(getAddress(dest));
                origen.setText(getAddress(sourc));

                rating.isIndicator();
                rating.setEnabled(Boolean.FALSE);
                Rating ratingD = trip.getDriverRating();
                if(ratingD != null){
                    rating.setNumStars(ratingD.getRating() != null ? ratingD.getRating().intValue() : 3);
                    comentariosDelViaje.setText(ratingD.getComment() != null && !ratingD.getComment().isEmpty()? ratingD.getComment() : "Sin comentarios");
                } else {
                    rating.setNumStars(3);
                    comentariosDelViaje.setText("Sin comentarios");
                }

                precio.setText("$".concat(trip.getPrice().toString()));


            }

            @Override
            public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent navigationIntent = new Intent(ChoferViewFinishedTripActivity.this,ChoferActivity.class);
                Bundle bundle = new Bundle();

                bundle.putLong("driverId",driverId );

                navigationIntent.putExtra("bundle", bundle);
                startActivity(navigationIntent);
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

    @Override
    public void onBackPressed() {

    }

}
