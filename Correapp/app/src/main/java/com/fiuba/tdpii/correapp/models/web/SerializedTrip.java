package com.fiuba.tdpii.correapp.models.web;

import com.fiuba.tdpii.correapp.models.web.Trip;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SerializedTrip {

    @SerializedName("trips")
    @Expose
    private Trip trips;

    public Trip getTrip() {
        return trips;
    }

    public void setTrip(Trip trips) {
        this.trips = trips;
    }
}