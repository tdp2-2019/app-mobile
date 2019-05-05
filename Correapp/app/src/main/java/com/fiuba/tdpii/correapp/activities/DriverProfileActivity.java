package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Rejected;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.driver.DriverPost;
import com.fiuba.tdpii.correapp.services.drivers.DriverService;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverProfileActivity extends AppCompatActivity {


    private DriverService driverService;
    private TripService tripService;


    private Bundle bundle;

    private TextView nombre;
    private TextView auto;
    private TextView patente;
    private TextView puntaje;
    private TextView viajesRealizados;
    private TextView antiguedad;

    private ImageView profile;

    private Button asignaciones;
    private String profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile_asignaciones);

        nombre = findViewById(R.id.nombre);
        auto = findViewById(R.id.auto);
        patente = findViewById(R.id.patente);
        puntaje = findViewById(R.id.puntaje);
        viajesRealizados = findViewById(R.id.viajes_realizados);
        antiguedad = findViewById(R.id.antiguedad);

        asignaciones = findViewById(R.id.listado_viajes);


        driverService = new DriverService();
        tripService = new TripService();

        DriverProfileActivity activity = this;


        bundle = getIntent().getParcelableExtra("bundle");


        Long driverId = bundle.getLong("driverId");

        profile = findViewById(R.id.imagen_perfil);


//        Glide.with(this).load(driver.getPhotoUrl()).into(profile);

        driverService.getDriverById(driverId.toString()).enqueue(new Callback<DriverPost>() {

            @Override
            public void onResponse(Call<DriverPost> call, Response<DriverPost> response) {

                DriverPost driver = response.body();

                if (driver.getPhotoUrl() != null && !driver.getPhotoUrl().equals(""))
                    Glide.with(activity).load(driver.getPhotoUrl()).into(profile);


                String nombreStr = driver.getName() + " " + driver.getLastname();
                nombre.setText(nombreStr);

                String autoStr = driver.getBrand() + " " + driver.getModel() + " " + driver.getCarcolour();
                auto.setText(autoStr);

                String ratingDriver = driver.getRating() != null ? String.format("%.3f", driver.getRating()) : "3.0";
                String puntajeStr = "Puntaje " + ratingDriver;
                puntaje.setText(puntajeStr);


                String signUp = driver.getSignupDate().substring(0, 10);

                //2019-04-22T00:00:00.000Z
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dateSignUp = format.parse(signUp);

                    Date now = new Date();

                    int months = monthsBetween(dateSignUp, now);

                    if (months == 0) {
                        int days = getDaysDifference(dateSignUp, now);
                        String antiguedadStr = days + " dias";
                        antiguedad.setText(antiguedadStr);
                    } else {
                        String antiguedadStr = months + " meses";
                        antiguedad.setText(antiguedadStr);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DriverPost> call, Throwable t) {

            }
        });


        tripService.getTripsDoneByDriver(driverId.toString()).enqueue(new Callback<List<SerializedTripPostResponse>>() {

            @Override
            public void onResponse(Call<List<SerializedTripPostResponse>> call, Response<List<SerializedTripPostResponse>> response) {

                if (response.code() == 404) {

                    String viajesRealizadosDriver = "0";
                    String viajesRealizadosStr = viajesRealizadosDriver + " viajes realizados";
                    viajesRealizados.setText(viajesRealizadosStr);
                } else {
                    List<SerializedTripPostResponse> trips = response.body();
                    String viajesRealizadosDriver = String.valueOf(trips.size());
                    String viajesRealizadosStr = viajesRealizadosDriver + " viajes realizados";
                    viajesRealizados.setText(viajesRealizadosStr);
                }

            }

            @Override
            public void onFailure(Call<List<SerializedTripPostResponse>> call, Throwable t) {

                String viajesRealizadosDriver = "0";
                String viajesRealizadosStr = viajesRealizadosDriver + " viajes realizados";
                viajesRealizados.setText(viajesRealizadosStr);
            }
        });
        asignaciones.setOnClickListener(v -> {

            Intent navigationIntent = new Intent(DriverProfileActivity.this, ChoferActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("driverId", driverId);
            navigationIntent.putExtra("bundle", bundle);
            startActivity(navigationIntent);

        });
    }

    static int monthsBetween(Date a, Date b) {
        Calendar cal = Calendar.getInstance();
        if (a.before(b)) {
            cal.setTime(a);
        } else {
            cal.setTime(b);
            b = a;
        }
        int c = 0;
        while (cal.getTime().before(b)) {
            cal.add(Calendar.MONTH, 1);
            c++;
        }
        return c - 1;
    }

    public static int getDaysDifference(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;

        return (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    @Override
    public void onBackPressed() {

    }

}
