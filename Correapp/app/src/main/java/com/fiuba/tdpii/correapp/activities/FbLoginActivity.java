package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.fiuba.tdpii.correapp.services.drivers.DriverService;
import com.fiuba.tdpii.correapp.services.trips.TripService;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FbLoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private LoginButton loginButton;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private Uri profileUrl;
    private DriverService driverService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        driverService = new DriverService();


        setContentView(R.layout.activity_fb_login);

        final AccessToken[] accessToken = {AccessToken.getCurrentAccessToken()};
        boolean isLoggedIn = accessToken[0] != null && !accessToken[0].isExpired();

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
                setFacebookData(loginResult);

            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                // App code

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response",response.toString());

                            email = response.getJSONObject().getString("email");
                            firstName = response.getJSONObject().getString("first_name");
                            lastName = response.getJSONObject().getString("last_name");



                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link",link);
                            if (Profile.getCurrentProfile()!=null)
                            {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                                profileUrl = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
                            }

                            Log.i("Login" + "Email", email);
                            Log.i("Login"+ "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);

                            Intent navigationIntent = new Intent(FbLoginActivity.this, CreateDriverActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("email", email);
                            bundle.putString("firstname", firstName );
                            bundle.putString("lastName", lastName );
                            navigationIntent.putExtra("bundle", bundle );
                            startActivity(navigationIntent);

//
//                            DriverPost driver = new DriverPost();
//                            driver.setAddress("Moldes 1423");
//                            driver.setDni("38305615");
//                            driver.setName(firstName);
//                            driver.setLastname(lastName);
//                            driver.setEmail(email);
//                            driver.setTelephone("47860146");
//                            driver.setCelphone("1562676821");
//                            driver.setBrand("Peugeot");
//                            driver.setModel("504");
//                            driver.setLicensenumber(420420420l);
//                            driver.setInsurancepolicynumber("ME COMERE A SUERTUDO");
//                            driver.setEndworktime("1555707893");
//                            driver.setStartworktime("1555679093");
//                            driver.setCarlicenseplate("ALT420");
//                            driver.setCarcolour("Crema");
//                            Destination currentPosition = new Destination();
//                            currentPosition.setLong("-58.454000");
//                            currentPosition.setLat("-34.568900");
//                            driver.setCurrentposition(currentPosition);
//
//                            driverService.saveNewDriver(driver).enqueue(new Callback<DriverPost>() {
//                                @Override
//                                public void onResponse(Call<DriverPost> call, Response<DriverPost> response) {
//
//                                    response.body();
//                                    Long driverId = response.body().getId();
//                                    Toast.makeText(FbLoginActivity.this, driverId.toString(), Toast.LENGTH_SHORT).show();
//
////                                    bundle.putLong("id",tripId );
////                                    navigationIntent.putExtra("bundle", bundle );
////                                    startActivity(navigationIntent);
//                                }
//
//                                @Override
//                                public void onFailure(Call<DriverPost> call, Throwable t) {
//
//                                }
//                            });
//
//
//
//

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,address,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
