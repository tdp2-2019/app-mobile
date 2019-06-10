package com.fiuba.tdpii.correapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Destination;
import com.fiuba.tdpii.correapp.models.web.driver.DriverPost;
import com.fiuba.tdpii.correapp.services.drivers.DriverService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDriverCarActivity extends AppCompatActivity {


    private static final Double DEFAULT_RANKING = 3d;
    private Bundle bundle;

    private final int PICK_IMAGE_PATENTE_REQUEST = 71;
    private final int PICK_IMAGE_REGISTRO_REQUEST = 72;

    private Uri patenteFilePath;
    private Uri registroFilePath;


    FirebaseStorage storage;
    StorageReference storageReference;


    private EditText marca;

    private EditText modelo;

    private EditText color;

    private EditText registro;

    private EditText seguro;

    private EditText patente;


    private Button uploadRegistro;
    private Button uploadPatente;

    private ImageButton patenteImage;
    private ImageButton registroImage;

    private Button finalizar;
    private DriverService driverService;
    private String profilePictureUri;
    private String patenteUri = "";
    private String registroUri = "";

    CreateDriverCarActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_driver_car);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        activity = this;

        driverService = new DriverService();

        bundle = getIntent().getParcelableExtra("bundle");

        profilePictureUri = bundle.getString("picture");


        marca = findViewById(R.id.marca);
        modelo = findViewById(R.id.modelo);
        color = findViewById(R.id.color);
        registro = findViewById(R.id.registro);
        seguro = findViewById(R.id.seguro);
        patente = findViewById(R.id.patente);

        uploadPatente = findViewById(R.id.subirPatente);
        uploadPatente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFullImagePatente();
            }
        });

        uploadRegistro = findViewById(R.id.subirRegistro);
        uploadRegistro.setOnClickListener( v -> {
            uploadFullImageRegistro();
        });

        patenteImage = findViewById(R.id.patentePicture);
        registroImage = findViewById(R.id.registroPicture);

        patenteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!patenteUri.equals("")) {
                    Intent patenteIntent = new Intent(CreateDriverCarActivity.this, PatenteActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("patente", patenteUri);

                    patenteIntent.putExtra("bundle", bundle);

                    startActivity(patenteIntent);
                }
            }
        });


        registroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!registroUri.equals("")) {

                    Intent registroIntent = new Intent(CreateDriverCarActivity.this, RegistroActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("registro", registroUri);

                    registroIntent.putExtra("bundle", bundle);
                    startActivity(registroIntent);
                }
            }
        });



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

            if(patenteUri.equals("")){
                Toast.makeText(CreateDriverCarActivity.this, "Te olvidaste de subir una foto de la patente de tu auto", Toast.LENGTH_LONG).show();
                return;
            }

            if(registroUri.equals("")){
                Toast.makeText(CreateDriverCarActivity.this, "Te olvidaste de subir una foto de tu registro de conducir", Toast.LENGTH_LONG).show();
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
            driver.setLicensePhotoUrl(registroUri);
            driver.setPatentePhotoUrl(patenteUri);

            driver.setRating(3d);
            Date cDate = new Date();
            String fDate = new SimpleDateFormat("MM-dd-yyyy").format(cDate);

            driver.setSignupDate(fDate);
            driver.setActive("S");

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

    private void uploadFullImageRegistro() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REGISTRO_REQUEST);
    }


    private void uploadFullImagePatente() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_PATENTE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_PATENTE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            patenteFilePath = data.getData();
            uploadImagePatente();
        } else if (requestCode == PICK_IMAGE_REGISTRO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            registroFilePath = data.getData();
            uploadImageRegistro();
        }
    }

    private void uploadImagePatente() {

        if (patenteFilePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo...");
            progressDialog.show();

            StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(patenteFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            patenteUri = taskSnapshot.getDownloadUrl().toString();
                            if (patenteUri != null)
                                Glide.with(activity).load(patenteUri).into(patenteImage);

                            Toast.makeText(CreateDriverCarActivity.this, "Subida", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CreateDriverCarActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Subiendo " + (int) progress + "%");
                        }
                    });
        }
    }

    private void uploadImageRegistro() {

        if (registroFilePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo...");
            progressDialog.show();

            StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(registroFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            registroUri = taskSnapshot.getDownloadUrl().toString();
                            if (registroUri != null)
                                Glide.with(activity).load(registroUri).into(registroImage);
                            Toast.makeText(CreateDriverCarActivity.this, "Subida", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CreateDriverCarActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Subiendo " + (int) progress + "%");
                        }
                    });
        }
    }
}
