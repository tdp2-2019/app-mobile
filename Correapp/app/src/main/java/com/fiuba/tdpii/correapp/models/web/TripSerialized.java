package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TripSerialized {

    @Expose
    @SerializedName("trips")
    public Trip trip;

    public static class Trip {
            @Expose
            @SerializedName("client")
            public String client;

            @Expose
            @SerializedName("_id")
            public Long id;

            @Expose
            @SerializedName("_start_time")
            public Long startTime;

            @Expose
            @SerializedName("_end_time")
            public Long endTime;

            @Expose
            @SerializedName("_pets")
            public String pets;


            @Expose
            @SerializedName("_status")
            public String status;

            @Expose
            @SerializedName("_duration")
            public Long duration;

            @Expose
            @SerializedName("_price")
            public Long price;

    }
}
