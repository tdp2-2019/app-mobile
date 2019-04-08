package com.fiuba.tdpii.correapp.activities;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.models.local.Pet;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private static final int NOW_TAB_POSITION = 0;
    private static final int LATER_TAB_POSITION = 1;

    private TabItem viajarAhora;
    private TabItem reservar;
    private TabLayout tabs;

    private TextView origen;
    private TextView destino;
    private TextView mascotas;

    private TextView fecha;
    private TextView hora;


    private Button reservarBtn;

    private Boolean tripNow = Boolean.TRUE;

    private Date tripDate;
    private Integer hours;
    private Integer minutes;

    private LatLng originLocation;
    private String orAddress;
    private LatLng destinyLocation;
    private String destAddress;

    private Pet mascota1;
    private Pet mascota2;
    private Pet mascota3;

    private Boolean paymentMethod;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);


        bundle = getIntent().getParcelableExtra("bundle");
        if (bundle != null) {
            originLocation = bundle.getParcelable("lc_origin");
            destinyLocation = bundle.getParcelable("lc_dest");
            orAddress = bundle.getString("add_origin");
            destAddress = bundle.getString("add_dest");


            mascota1 = bundle.getParcelable("pet_1");
            mascota2 = bundle.getParcelable("pet_2");

            mascota3 = bundle.getParcelable("pet_3");

            Long fechaUnix = bundle.getLong("fecha_unix");
            if (fechaUnix!= null && fechaUnix != 0l)
                tripDate = new Date(fechaUnix);
            Long hl = bundle.getLong("hora");
            if(hl != null && hl != 0l)
                hours = hl.intValue();
            Long ml = bundle.getLong("minutos");
            if (ml != null && ml != 0l)
                minutes = ml.intValue();

            if (bundle.containsKey("viajar_ahora") ){
                tripNow = bundle.getBoolean("viajar_ahora");
            }
        }



        viajarAhora = findViewById(R.id.trip_now);

        reservar = findViewById(R.id.reservar);
        tabs = findViewById(R.id.tabLayout);


        origen = findViewById(R.id.origen);
        origen.setText(orAddress);
        destino = findViewById(R.id.destino);
        destino.setText(destAddress);
        mascotas = findViewById(R.id.mascotas);
        String petNames = "Mascotas ";
        if (mascota1 != null){
            petNames = "Viaja " + mascota1.nombre;
        }
        if(mascota2 != null){
            petNames = petNames.concat(" junto a " + mascota2.nombre);
        }
        if(mascota3 != null){
            petNames = petNames.concat(" y a " + mascota3.nombre);
        }

        mascotas.setText(petNames);

        fecha = findViewById(R.id.fecha);
        hora = findViewById(R.id.hora);

        hora.setText("");
        hora.setClickable(Boolean.FALSE);
        fecha.setText("");
        fecha.setClickable(Boolean.FALSE);


        reservarBtn = findViewById(R.id.confirm);
        if(tripNow) {

            fecha.setVisibility(View.GONE);
            hora.setVisibility(View.GONE);
            hora.setText("");
            hora.setClickable(Boolean.FALSE);

            fecha.setText("");
            fecha.setClickable(Boolean.FALSE);

                tabs.getTabAt(0).select();
            } else {
                fecha.setVisibility(View.VISIBLE);
                hora.setVisibility(View.VISIBLE);

                hora.setText(hours != null ? "A las " + hours + " y " + minutes
                        : "Hora");
                hora.setClickable(Boolean.TRUE);

                String dateStr = "Fecha";
                if (tripDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(tripDate);
                    dateStr = "El " + calendar.get(Calendar.DAY_OF_MONTH) + " de " + theMonth(calendar.get(Calendar.MONTH));
                }
                fecha.setText(dateStr);
                fecha.setClickable(Boolean.TRUE);
                tabs.getTabAt(1).select();

            }



        setUpEvents();

    }

    private void setUpEvents(){

//        viajarAhora.setZ(viajarAhora.getTranslationZ() + 100f);
//        viajarAhora.setTextColor(Color.WHITE);
//        viajarAhora.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(paraReservar) {
//                    viajarAhora.setHighlightColor(Color.WHITE);
//                    reservar.setHighlightColor(R.color.black_overlay);
//
//                    viajarAhora.setZ(viajarAhora.getTranslationZ() + 100f);
//                    paraReservar = !paraReservar;
//                }
//
//            }
//        });
//        reservar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(!paraReservar) {
//                    reservar.setHighlightColor(Color.WHITE);
//                    viajarAhora.setHighlightColor(R.color.black_overlay);
//
//                    reservar.setZ(viajarAhora.getTranslationZ() + 100f);
//                    paraReservar = !paraReservar;
//                }
//
//            }
//        });

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position){
                    case NOW_TAB_POSITION:
//
//                        fecha.setVisibility(View.GONE);
//                        hora.setVisibility(View.GONE);
                        hora.setText("");
                        hora.setClickable(Boolean.FALSE);

                        fecha.setText("");
                        fecha.setClickable(Boolean.FALSE);

                        tripNow = Boolean.TRUE;

                        break;
                    case LATER_TAB_POSITION:
                        fecha.setVisibility(View.VISIBLE);
                        hora.setVisibility(View.VISIBLE);

                        hora.setText(hours != null ?  "A las "  + hours + " y " + minutes
                                :"Hora");
                        hora.setClickable(Boolean.TRUE);

                        String dateStr = "Fecha";
                        if(tripDate != null){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(tripDate);
                            dateStr = "El " +  calendar.get(Calendar.DAY_OF_MONTH) + " de "  + theMonth(calendar.get(Calendar.MONTH));
                        }

                        fecha.setText(dateStr);
                        fecha.setClickable(Boolean.TRUE);

                        tripNow = Boolean.FALSE;

                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                                hours = hourOfDay;
                                minutes = minute;
                                hora.setText("A las "  + hours + " y " + minutes);

                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
//                Timepoint mTimepoint = new Timepoint( now.get(Calendar.HOUR),now.get(Calendar.MINUTE),now.get(Calendar.SECOND));
//                tpd.setMinTime(mTimepoint);
                tpd.show(getFragmentManager(), "TimePickerDialog");

            }
        });


        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateTripActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );


                Calendar mcurrentDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                maxDate.add(Calendar.YEAR,1 );


                dpd.setMinDate(mcurrentDate);
                dpd.setMaxDate(maxDate);


                dpd.show(getFragmentManager(), "DatePicker");
            }
        });


        mascotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent navigationIntent = new Intent(CreateTripActivity.this, AddPetActivity.class);
                Bundle bundle = new Bundle();

                if(originLocation != null)
                    bundle.putParcelable("lc_origin",  originLocation);
                if(destinyLocation != null)
                    bundle.putParcelable("lc_dest",  destinyLocation);
                if(orAddress != null)
                    bundle.putString("add_origin", orAddress);
                if(destAddress != null)
                    bundle.putString("add_dest", destAddress);

                bundle.putBoolean("viajar_ahora", tripNow );

                bundle.putParcelable("pet_1", mascota1 );
                bundle.putParcelable("pet_2", mascota2 );
                bundle.putParcelable("pet_3", mascota3 );

                if(tripDate != null)
                    bundle.putLong("fecha_unix",  tripDate.getTime());

                if(hours != null)
                    bundle.putLong("hora", hours);
                if(minutes != null)
                    bundle.putLong("minutos", minutes);

                navigationIntent.putExtra("bundle", bundle );
                startActivity(navigationIntent);


            }
        });

        reservarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mascota1 == null){
                    Toast.makeText(CreateTripActivity.this, "Te olvidaste de agregar a tus mascotas" , Toast.LENGTH_LONG).show();
                    return;
                }

                if(!tripNow) {
                    if (tripDate == null) {
                        Toast.makeText(CreateTripActivity.this, "Te olvidaste el día de la reserva", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (hours == null) {
                        Toast.makeText(CreateTripActivity.this, "Te olvidaste la hora de la reserva", Toast.LENGTH_LONG).show();
                        return;
                    }
                }




            }
        });

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Date date = new Date(year,monthOfYear ,dayOfMonth );
        tripDate = date;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tripDate);


        fecha.setText("El " +  calendar.get(Calendar.DAY_OF_MONTH) + " de "  + theMonth(calendar.get(Calendar.MONTH)));

    }

    public static String theMonth(int month){
        String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return monthNames[month];
    }
}
