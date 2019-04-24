package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Destination;
import com.fiuba.tdpii.correapp.models.web.driver.DriverPost;
import com.fiuba.tdpii.correapp.services.drivers.DriverService;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDriverCarActivity extends AppCompatActivity {


    private static final Double DEFAULT_RANKING = 3d;
    private Bundle bundle;


    private EditText marca;

    private EditText modelo;

    private EditText color;

    private EditText registro;

    private EditText seguro;

    private EditText patente;

    private Button finalizar;
    private DriverService driverService;
    private String profilePictureUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_driver_car);

        driverService = new DriverService();

        bundle = getIntent().getParcelableExtra("bundle");

        profilePictureUri = bundle.getString("picture");


        marca = findViewById(R.id.marca);
        modelo = findViewById(R.id.modelo);
        color = findViewById(R.id.color);
        registro = findViewById(R.id.registro);
        seguro = findViewById(R.id.seguro);
        patente = findViewById(R.id.patente);

        finalizar = findViewById(R.id.confirm);

        finalizar.setOnClickListener(v -> {


            if (marca.getText().toString().equals("")) {
                Toast.makeText(CreateDriverCarActivity.this, "Te olvidaste de agregar la marca de tu auto", Toast.LENGTH_LONG).show();
                return;
            }
            if (modelo.getText().toString().equals("")) {
                Toast.makeText(CreateDriverCarActivity.this, "Te olvidaste de agregar el modelo de tu auto", Toast.LENGTH_LONG).show();
                return;
            }
            if (color.getText().toString().equals("")) {
                Toast.makeText(CreateDriverCarActivity.this, "Te olvidaste de agregar el color de tu auto", Toast.LENGTH_LONG).show();
                return;
            }
            if (registro.getText().toString().equals("")) {
                Toast.makeText(CreateDriverCarActivity.this, "Te olvidaste de agregar tu registro de conducir", Toast.LENGTH_LONG).show();
                return;
            }
            if (seguro.getText().toString().equals("")) {
                Toast.makeText(CreateDriverCarActivity.this, "Te olvidaste de agregar tu numero de seguro", Toast.LENGTH_LONG).show();
                return;
            }
            if (patente.getText().toString().equals("")) {
                Toast.makeText(CreateDriverCarActivity.this, "Te olvidaste de agregar la patente de tu auto", Toast.LENGTH_LONG).show();
                return;
            }


            DriverPost driver = new DriverPost();
            driver.setAddress(bundle.getString("direccion"));
            driver.setDni(bundle.getString("dni"));
            driver.setName(bundle.getString("nombre"));
            driver.setLastname(bundle.getString("apellido"));
            driver.setEmail(bundle.getString("email"));
            driver.setTelephone(bundle.getString("tel"));
            driver.setCelphone(bundle.getString("cel"));
            driver.setBrand(marca.getText().toString());
            driver.setModel(modelo.getText().toString());
            driver.setLicensenumber(Long.parseLong(registro.getText().toString()));
            driver.setInsurancepolicynumber(registro.getText().toString());
            driver.setEndworktime(null);
            driver.setStartworktime(String.valueOf(new Date().getTime()));
            driver.setCarlicenseplate(patente.getText().toString());
            driver.setCarcolour(color.getText().toString());

            driver.setPhotoUrl(profilePictureUri);



            Date cDate = new Date();
            String fDate = new SimpleDateFormat("MM-dd-yyyy").format(cDate);

            driver.setSignupDate(fDate);

            LatLng currentPosition = bundle.getParcelable("currentPosition");

            Destination originLocation = new Destination();
            originLocation.setLat(String.valueOf(currentPosition.latitude));
            originLocation.setLong(String.valueOf(currentPosition.longitude));


            driver.setCurrentposition(originLocation);

            driverService.saveNewDriver(driver).enqueue(new Callback<DriverPost>() {
                @Override
                public void onResponse(Call<DriverPost> call, Response<DriverPost> response) {

                    response.body();
                    Long driverId = response.body().getId();
//                    Toast.makeText(CreateDriverCarActivity.this, driverId.toString(), Toast.LENGTH_SHORT).show();


                    Intent navigationIntent = new Intent(CreateDriverCarActivity.this, DriverProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("picture",profilePictureUri );

                    bundle.putLong("driverId", driverId );
                    navigationIntent.putExtra("bundle", bundle);

                    startActivity(navigationIntent);

                }

                @Override
                public void onFailure(Call<DriverPost> call, Throwable t) {

                }
            });


        });

    }
}
