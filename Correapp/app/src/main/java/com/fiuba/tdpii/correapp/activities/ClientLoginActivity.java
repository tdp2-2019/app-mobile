package com.fiuba.tdpii.correapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Destination;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.driver.DriverPost;
import com.fiuba.tdpii.correapp.models.web.user.ClientResponse;
import com.fiuba.tdpii.correapp.services.drivers.DriverService;
import com.fiuba.tdpii.correapp.services.trips.TripService;
import com.fiuba.tdpii.correapp.services.users.UserService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientLoginActivity extends AppCompatActivity {


    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private LoginButton loginButton;
    private String email;
    private String firstName;
    private String lastName;
    private Uri profileUrl;
    private UserService userService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        userService = new UserService();

        setContentView(R.layout.activity_client_login);


        final AccessToken[] accessToken = {AccessToken.getCurrentAccessToken()};
        boolean isLoggedIn = accessToken[0] != null && !accessToken[0].isExpired();


        if (isLoggedIn) {
            setFacebookData(accessToken[0]);
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        accessToken[0] = loginResult.getAccessToken();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(ClientLoginActivity.this, "Error al conectarse con Facebook", Toast.LENGTH_SHORT).show();
                    }
                });

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                accessToken[0] = loginResult.getAccessToken();
                boolean isLoggedIn = accessToken[0] != null && !accessToken[0].isExpired();
                if (isLoggedIn) {
                    setFacebookData(accessToken[0]);
                }
            }


            @Override
            public void onCancel() {
                // App code
                System.out.print("ln");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(ClientLoginActivity.this, "Error al conectarse con Facebook", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setFacebookData(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response", response.toString());

                            email = response.getJSONObject().getString("email");
                            firstName = response.getJSONObject().getString("first_name");
                            lastName = response.getJSONObject().getString("last_name");


                            Profile profile = Profile.getCurrentProfile();

                            Log.i("Login" + "Email", email);
                            Log.i("Login" + "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);


                            userService.getUsersByEmail(email).enqueue(new Callback<List<ClientResponse>>() {
                                @Override
                                public void onResponse(Call<List<ClientResponse>> call, Response<List<ClientResponse>> response) {
                                    if (response.code() == 404) {
                                        Intent navigationIntent = new Intent(ClientLoginActivity.this, CreateClientActivity.class);
                                        Bundle bundle = new Bundle();


                                        bundle.putString("picture", profile != null ? profile.getProfilePictureUri(360, 360).toString() : "https://graph.facebook.com/v3.2/10219407561286759/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");
                                        bundle.putString("email", email);
                                        bundle.putString("firstname", firstName);
                                        bundle.putString("lastName", lastName);
                                        navigationIntent.putExtra("bundle", bundle);
                                        startActivity(navigationIntent);
                                    } else {

                                        List<ClientResponse> users = response.body();
                                        ClientResponse user = users.get(0);

                                        Long clientId = user.getId();

                                        Intent navigationIntent = new Intent(ClientLoginActivity.this, MapHomeActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putLong("clientId", clientId);
                                        bundle.putString("client", user.getName());
                                        navigationIntent.putExtra("bundle", bundle);
                                        startActivity(navigationIntent);

                                    }

                                }

                                @Override
                                public void onFailure(Call<List<ClientResponse>> call, Throwable t) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }
}

