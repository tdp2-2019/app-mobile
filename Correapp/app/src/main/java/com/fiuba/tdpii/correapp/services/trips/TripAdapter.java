package com.fiuba.tdpii.correapp.services.trips;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fiuba.tdpii.correapp.R;
import com.fiuba.tdpii.correapp.activities.AddPetActivity;
import com.fiuba.tdpii.correapp.activities.ChoferActivity;
import com.fiuba.tdpii.correapp.activities.ChoferViewTripActivity;
import com.fiuba.tdpii.correapp.activities.CreateTripActivity;
import com.fiuba.tdpii.correapp.models.web.SerializedTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripAdapter extends ArrayAdapter<SerializedTripPostResponse> {
    private Context mcon;


    public TripAdapter(Context context, List<SerializedTripPostResponse> trips) {
        super(context, 0, trips);
        mcon = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final SerializedTripPostResponse trip = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trip_view, parent, false);
        }

        TextView fechaView = convertView.findViewById(R.id.fecha);


        String sDate1=trip.getStartTime().substring(0,10);
        Date startDate= null;
        try {
            //2019-04-22T00:46:50.000Z
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        String dateStr = "El " + calendar.get(Calendar.DAY_OF_MONTH) + " de " + theMonth(calendar.get(Calendar.MONTH));

        fechaView.setText(dateStr);

        TextView nameView = convertView.findViewById(R.id.nombre);
        nameView.setText(trip.getClient());

        TextView id = convertView.findViewById(R.id.precio);
        id.setText("$ ".concat(String.format(Locale.ITALY, "%,d", trip.getPrice().longValue())));

        return convertView;
    }

    public static String theMonth(int month){
        String[] monthNames = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return monthNames[month];
    }


}
