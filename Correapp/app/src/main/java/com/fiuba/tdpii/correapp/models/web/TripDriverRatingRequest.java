package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripDriverRatingRequest {
    @SerializedName("driver_rating")
    @Expose
    private DriverRating driverRating;

    public DriverRating getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(DriverRating driverRating) {
        this.driverRating = driverRating;
    }
}
