package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SerializedTripPostResponse {

    @SerializedName("trip")
    @Expose
    private TripResponse trips;

    public TripResponse getTrip() {
        return trips;
    }

    public void setTrip(TripResponse trips) {
        this.trips = trips;
    }
}