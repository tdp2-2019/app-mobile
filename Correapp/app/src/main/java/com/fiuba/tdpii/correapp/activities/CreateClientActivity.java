package com.fiuba.tdpii.correapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.user.ClientResponse;
import com.fiuba.tdpii.correapp.services.users.UserService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClientActivity extends AppCompatActivity {


    private final int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    UserService userService;

    private EditText dni;

    private EditText nombre;

    private EditText apellido;

    private EditText emailView;

    private EditText cel;

    private EditText tel;

    private EditText direccion;

    private LatLng currentPosition;

    private ImageView backArrow;

    private Button continueButton;
    CreateClientActivity activity;

    private Bundle bundle;

    private String email;
    private String firstName;
    private String lastName;
    private String profilePictureUri;

    private ImageView profile;

    private Button uploadProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client);

        activity = this;


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        userService = new UserService();

        bundle = getIntent().getParcelableExtra("bundle");


        email = bundle.getString("email");
        firstName = bundle.getString("firstname");
        lastName = bundle.getString("lastName");


        nombre = findViewById(R.id.nombre);
        nombre.setText(firstName);


        apellido = findViewById(R.id.apellido);
        apellido.setText(lastName);

        continueButton = findViewById(R.id.confirm);

        setUpEvents();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadImage();
        }
    }


    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo...");
            progressDialog.show();

            StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            profilePictureUri = taskSnapshot.getDownloadUrl().toString();

                            if (profilePictureUri != null)
                                Glide.with(activity).load(profilePictureUri).into(profile);


                            Toast.makeText(CreateClientActivity.this, "Subida", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CreateClientActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setUpEvents() {


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombre.getText().toString().equals("")) {
                    Toast.makeText(CreateClientActivity.this, "Te olvidaste de agregar tu nombre", Toast.LENGTH_LONG).show();
                    return;

                }
                if (apellido.getText().toString().equals("")) {
                    Toast.makeText(CreateClientActivity.this, "Te olvidaste de agregar tu apellido", Toast.LENGTH_LONG).show();

                    return;
                }




                ClientResponse client = new ClientResponse();
                client.setAddress("a");
                client.setCelphone("15616828332");
                client.setDni("3849772");
                client.setEmail(email);
                client.setLastname(apellido.getText().toString());
                client.setName(nombre.getText().toString());
                client.setTelephone("47860146");
                client.setRating(3f);
                String token =  FirebaseInstanceId.getInstance().getToken();

                client.setFirebaseId(token);



                userService.saveNewUser(client).enqueue(new Callback<ClientResponse>() {
                    @Override
                    public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                        ClientResponse user = response.body();

                        Intent navigationIntent = new Intent(CreateClientActivity.this, MapHomeActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("client", user.getName());
                        bundle.putLong("clientId", user.getId() );

                        navigationIntent.putExtra("bundle", bundle);
                        startActivity(navigationIntent);


                    }

                    @Override
                    public void onFailure(Call<ClientResponse> call, Throwable t) {

                    }
                });


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
