package com.fiuba.tdpii.correapp.models.web;

import java.util.List;

import com.fiuba.tdpii.correapp.models.web.Destination;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trip {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("client")
    @Expose
    private String client;
    @SerializedName("destination")
    @Expose
    private Destination destination;

    @SerializedName("source")
    @Expose
    private Destination source;

    public Destination getSource() {
        return source;
    }

    public void setSource(Destination source) {
        this.source = source;
    }

    @SerializedName("duration")
    @Expose
    private Long duration;
    @SerializedName("pets")
    @Expose
    private String pets;
    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("client_rating")
    @Expose
    private ClientRating clientRating;
    @SerializedName("driver_id")
    @Expose
    private Long driverId;
    @SerializedName("start_time")
    @Expose
    private Long startTime;
    @SerializedName("end_time")
    @Expose
    private Object endTime;
    @SerializedName("rejecteds")
    @Expose
    private List<Object> rejecteds = null;

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }



    public ClientRating getClientRating() {
        return clientRating;
    }

    public void setClientRating(ClientRating clientRating) {
        this.clientRating = clientRating;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public List<Object> getRejecteds() {
        return rejecteds;
    }

    public void setRejecteds(List<Object> rejecteds) {
        this.rejecteds = rejecteds;
    }

}