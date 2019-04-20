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

import java.util.ArrayList;

public class TripAdapter extends ArrayAdapter<SerializedTripPostResponse> {
    private Context mcon;


    public TripAdapter(Context context, ArrayList<SerializedTripPostResponse> trips) {
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
        TextView nameView = convertView.findViewById(R.id.nombre);
        nameView.setText(trip.getClient());

        TextView dest = convertView.findViewById(R.id.destino);
        dest.setText(trip.getDestination().getLat() + trip.getDestination().getLong());




        return convertView;
    }


}
