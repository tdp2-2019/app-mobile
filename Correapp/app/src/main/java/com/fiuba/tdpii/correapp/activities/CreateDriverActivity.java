package com.fiuba.tdpii.correapp.activities;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.local.PetLocal;
import com.fiuba.tdpii.correapp.models.web.Destination;
import com.fiuba.tdpii.correapp.models.web.Pet;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.TripPost;
import com.fiuba.tdpii.correapp.models.web.TripResponse;
import com.fiuba.tdpii.correapp.services.drivers.DriverService;
import com.fiuba.tdpii.correapp.services.trips.TripClient;
import com.fiuba.tdpii.correapp.services.trips.TripService;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDriverActivity extends AppCompatActivity {


    private EditText dni;

    private EditText nombre;

    private EditText apellido;

    private EditText userName;

    private EditText emailView;

    private EditText cel;

    private EditText tel;

    private EditText direccion;

    private LatLng currentPosition;

    private ImageView backArrow;

    private Button continueButton;

    private Bundle bundle;

    private String email;
    private String firstName;
    private String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_driver);


        bundle = getIntent().getParcelableExtra("bundle");


        email = bundle.getString("email");
        firstName = bundle.getString("firstname");
        lastName = bundle.getString("lastName");


        dni = findViewById(R.id.dni);

        nombre = findViewById(R.id.nombre);
        nombre.setText(firstName);


        apellido = findViewById(R.id.apellido);
        apellido.setText(lastName);


        emailView = findViewById(R.id.email);
        emailView.setText(email);


        userName = findViewById(R.id.user);


        cel = findViewById(R.id.cel);

        tel = findViewById(R.id.tel);

        direccion = findViewById(R.id.direccion);


        backArrow = findViewById(R.id.back_arrow);

        continueButton = findViewById(R.id.confirm);


        backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setUpEvents();

    }

    private void setUpEvents() {


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (dni.getText().toString().equals("")) {
                    Toast.makeText(CreateDriverActivity.this, "Te olvidaste de agregar tu dni", Toast.LENGTH_LONG).show();
                    return;
                }
                if (nombre.getText().toString().equals("")) {
                    Toast.makeText(CreateDriverActivity.this, "Te olvidaste de agregar tu nombre", Toast.LENGTH_LONG).show();
                    return;

                }
                if (apellido.getText().toString().equals("")) {
                    Toast.makeText(CreateDriverActivity.this, "Te olvidaste de agregar tu apellido", Toast.LENGTH_LONG).show();

                    return;
                }
                if (userName.getText().toString().equals("")) {
                    Toast.makeText(CreateDriverActivity.this, "Te olvidaste de agregar tu nombre de usuario", Toast.LENGTH_LONG).show();

                    return;
                }
                if (emailView.getText().toString().equals("")) {
                    Toast.makeText(CreateDriverActivity.this, "Te olvidaste de agregar tu email", Toast.LENGTH_LONG).show();

                    return;
                }
                if (cel.getText().toString().equals("")) {
                    Toast.makeText(CreateDriverActivity.this, "Te olvidaste de agregar tu celular", Toast.LENGTH_LONG).show();

                    return;
                }
                if (tel.getText().toString().equals("")) {
                    Toast.makeText(CreateDriverActivity.this, "Te olvidaste de agregar tu telefono", Toast.LENGTH_LONG).show();

                    return;
                }
                if (direccion.getText().toString().equals("")) {
                    Toast.makeText(CreateDriverActivity.this, "Te olvidaste de agregar tu direccion", Toast.LENGTH_LONG).show();
                    return;
                }

                currentPosition = getLocation(direccion.getText().toString());

                if ( currentPosition  == null){
                    Toast.makeText(CreateDriverActivity.this, "Ingrese una direccion valida", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent navigationIntent = new Intent(CreateDriverActivity.this, CreateDriverCarActivity.class);
                Bundle bundle = new Bundle();

//
//                bundle.putBoolean("viajar_ahora", tripNow);
//
//                bundle.putParcelable("pet_1", mascota1);
//                bundle.putParcelable("pet_2", mascota2);
//                bundle.putParcelable("pet_3", mascota3);
//
//
//                bundle.putLong("id", tripId);

                bundle.putString("dni",  dni.getText().toString());
                bundle.putString("nombre",  nombre.getText().toString());
                bundle.putString("apellido",  apellido.getText().toString());
                bundle.putString("userName",  userName.getText().toString());
                bundle.putString("email",  emailView.getText().toString());

                bundle.putString("cel",  cel.getText().toString());

                bundle.putString("tel",  tel.getText().toString());

                bundle.putString("direccion",  direccion.getText().toString());

                bundle.putString("nombre",  nombre.getText().toString());
                bundle.putParcelable("currentPosition", currentPosition);

                navigationIntent.putExtra("bundle", bundle);
                startActivity(navigationIntent);


            }
        });

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

}
