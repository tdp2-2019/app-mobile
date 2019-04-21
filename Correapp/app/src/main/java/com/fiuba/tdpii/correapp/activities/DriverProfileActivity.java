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

import com.bumptech.glide.Glide;
import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.driver.DriverPost;
import com.fiuba.tdpii.correapp.services.drivers.DriverService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverProfileActivity extends AppCompatActivity {


    private DriverService driverService;

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





        bundle = getIntent().getParcelableExtra("bundle");


        profileUri = bundle.getString("picture");
        Long driverId = bundle.getLong("driverId");

        profile = findViewById(R.id.imagen_perfil);

        Glide.with(this).load(profileUri).into(profile);


        driverService.getDriverById(driverId.toString()).enqueue(new Callback<DriverPost>() {

            @Override
            public void onResponse(Call<DriverPost> call, Response<DriverPost> response) {

                DriverPost driver = response.body();

                String nombreStr = driver.getName() + " " + driver.getLastname();
                nombre.setText(nombreStr);

                String autoStr = driver.getBrand() + " " + driver.getModel() + " " + driver.getCarcolour();
                auto.setText(autoStr);

                String ratingDriver = driver.getRating()!= null ? driver.getRating().toString() : "3.0";
                String puntajeStr = "Puntaje " + ratingDriver;
                puntaje.setText(puntajeStr);

                String viajesRealizadosDriver = "189";
                String viajesRealizadosStr = viajesRealizadosDriver + " viajes realizados";
                viajesRealizados.setText(viajesRealizadosStr);

                String workTimeStr = driver.getStartworktime();
//                Long workTime = Long.parseLong(workTimeStr);

//                Date now = Calendar.getInstance().getTime();

                String antiguedadStr = "Antiguedad: 3 meses";
//                int diffInDays = (int)( (now.getTime() - workTime)
//                        / (1000 * 60 * 60 * 24) );
//
//                if(diffInDays < 30){
//                    antiguedadStr = antiguedadStr + diffInDays + " días";
//                } else{
//                    antiguedadStr = antiguedadStr + (int) Math.ceil((double)diffInDays / 30) + " meses";
//                }
                antiguedad.setText(antiguedadStr);

            }

            @Override
            public void onFailure(Call<DriverPost> call, Throwable t) {

            }
        });

        asignaciones.setOnClickListener(v -> {

            Intent navigationIntent = new Intent(DriverProfileActivity.this, ChoferActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("driverId", driverId );
            navigationIntent.putExtra("bundle", bundle);
            startActivity(navigationIntent);

        });
    }

}
