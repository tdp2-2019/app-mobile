package com.fiuba.tdpii.correapp.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.web.Rating;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.driver.DriverPost;
import com.fiuba.tdpii.correapp.services.drivers.DriverService;
import com.fiuba.tdpii.correapp.services.trips.TripService;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDriverProfileActivity extends AppCompatActivity {

    private DriverService driverService;
    private TripService tripService;

    private Bundle bundle;

    private TextView nombre;
    private TextView auto;
    private TextView patente;
    private RatingBar puntaje;
    private TextView viajesRealizados;
    private TextView antiguedad;

    private ImageView profile;


    private LatLng originLocation;
    private LatLng destinynLocation;
    private Long driverId;
    private Long tripId;

    private CountDownTimer timer = new CountDownTimer(30000000, 3000) {

        @Override
        public void onTick(long millisUntilFinished) {
            yourMethod();
        }

        @Override
        public void onFinish() {
            try {

            } catch (Exception e) {
                Log.e("Error", "Error: " + e.toString());
            }
        }
    }.start();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        nombre = findViewById(R.id.nombre);
        auto = findViewById(R.id.auto);
        patente = findViewById(R.id.patente);
        puntaje = findViewById(R.id.rating_bar);
        viajesRealizados = findViewById(R.id.viajes_realizados);
        antiguedad = findViewById(R.id.antiguedad);

        profile = findViewById(R.id.imagen_perfil);


        driverService = new DriverService();
        tripService = new TripService();

        ClientDriverProfileActivity activity = this;

        bundle = getIntent().getParcelableExtra("bundle");

        driverId = bundle.getLong("driverId");
        tripId = bundle.getLong("tripId");
        originLocation = bundle.getParcelable("lc_origin");
        destinynLocation = bundle.getParcelable("lc_dest");


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


                puntaje.setRating(driver.getRating()!= null ? driver.getRating().intValue() / 2 : 3);



                String workTimeStr = driver.getStartworktime();
//                Long workTime = Long.parseLong(workTimeStr);

//                Date now = Calendar.getInstance().getTime();

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
//
//        tripService.getTrips().enqueue(new Callback<List<SerializedTripPostResponse>>() {
//            @Override
//            public void onResponse(Call<List<SerializedTripPostResponse>> call, Response<List<SerializedTripPostResponse>> response) {
//
//                List<SerializedTripPostResponse> tripResponseArrayList = response.body();
//
//                Integer cantidadDeViajes = 0;
//                for (SerializedTripPostResponse trip : tripResponseArrayList) {
//                    if (trip.getStatus().equals("finished") && trip.getDriverId().equals(driverId)) {
//                        cantidadDeViajes++;
//                    }
//                }
//
//                String viajesRealizadosStr = cantidadDeViajes + " viajes realizados";
//                viajesRealizados.setText(viajesRealizadosStr);
//
//            }
//
//            @Override
//            public void onFailure(Call<List<SerializedTripPostResponse>> call, Throwable t) {
//
//            }
//        });


//        //Esperando status started
//        Handler handler = new Handler();
//        Runnable runnableCode = new Runnable() {
//            @Override
//            public void run() {
//
//                tripService.getTripById(tripId.toString()).enqueue(new Callback<SerializedTripPostResponse>() {
//                    @Override
//                    public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {
//
//                        response.body();
//
//                        if(response.body().getStatus().equals("started")){
//
//
//                            Intent navigationIntent = new Intent(ClientDriverProfileActivity.this, SeguimientoActivity.class);
//
//                            Bundle bundle = new Bundle();
//
//                            if(originLocation != null)
//                                bundle.putParcelable("lc_origin",  originLocation);
//                            if(destinynLocation != null)
//                                bundle.putParcelable("lc_dest",  destinynLocation);
//                            bundle.putLong("tripId",tripId );
//                            bundle.putLong("driverId", response.body().getDriverId());
//
//                            navigationIntent.putExtra("bundle", bundle );
//                            startActivity(navigationIntent);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {
//
//                    }
//                });
//
//                // Do something here on the main thread
//                Log.d("Handlers", "Called on main thread");
//            }
//        };
//
//        handler.postDelayed(runnableCode, 2000);
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

    }

    private void yourMethod() {
        if (tripId == null)
            return;
        tripService.getTripById(tripId.toString()).enqueue(new Callback<SerializedTripPostResponse>() {
            @Override
            public void onResponse(Call<SerializedTripPostResponse> call, Response<SerializedTripPostResponse> response) {

                response.body();

                if(response.body().getStatus().equals("started")){


                    Intent navigationIntent = new Intent(ClientDriverProfileActivity.this, SeguimientoActivity.class);

                    Bundle bundle = new Bundle();

                    if(originLocation != null)
                        bundle.putParcelable("lc_origin",  originLocation);
                    if(destinynLocation != null)
                        bundle.putParcelable("lc_dest",  destinynLocation);
                    bundle.putLong("tripId",tripId );
                    bundle.putLong("driverId", response.body().getDriverId());

                    navigationIntent.putExtra("bundle", bundle );
                    timer.cancel();
                    startActivity(navigationIntent);
                }
            }

            @Override
            public void onFailure(Call<SerializedTripPostResponse> call, Throwable t) {

            }
        });

        // Do something here on the main thread
        Log.d("Handlers", "Called on main thread");
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
