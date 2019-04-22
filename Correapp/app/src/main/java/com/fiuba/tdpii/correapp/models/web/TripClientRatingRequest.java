package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripClientRatingRequest {
    @SerializedName("user_rating")
    @Expose
    private Rating rating;

    public Rating getRating() { return rating; }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
