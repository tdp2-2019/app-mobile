package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripPost {


    @SerializedName("client")
    @Expose
    private String client;
    @SerializedName("destination")
    @Expose
    private Destination destination;
    @SerializedName("source")
    @Expose
    private Destination source;


    @SerializedName("pets")
    @Expose
    private String pets;



    @SerializedName("start_time")
    @Expose
    private String startTime;


    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }


    public Destination getSource() {
        return source;
    }

    public void setSource(Destination source) {
        this.source = source;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }



}