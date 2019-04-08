package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TripResponse {

    @SerializedName("_id")
    @Expose
    private Long id;
    @SerializedName("_client")
    @Expose
    private String client;
    @SerializedName("_source")
    @Expose
    private Destination source;
    @SerializedName("_destination")
    @Expose
    private Destination destination;
    @SerializedName("_start_time")
    @Expose
    private Long startTime;
    @SerializedName("_end_time")
    @Expose
    private Object endTime;
    @SerializedName("_driver_id")
    @Expose
    private Object driverId;
    @SerializedName("_rejecteds")
    @Expose
    private List<Object> rejecteds = null;

    @SerializedName("_pets")
    @Expose
    private String pets;

    @SerializedName("_client_rating")
    @Expose
    private ClientRating clientRating;
    @SerializedName("_status")
    @Expose
    private String status;
    @SerializedName("_points")
    @Expose
    private List<List<Float>> points = null;
    @SerializedName("_duration")
    @Expose
    private Long duration;

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

    public Destination getSource() {
        return source;
    }

    public void setSource(Destination source) {
        this.source = source;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
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

    public Object getDriverId() {
        return driverId;
    }

    public void setDriverId(Object driverId) {
        this.driverId = driverId;
    }

    public List<Object> getRejecteds() {
        return rejecteds;
    }

    public void setRejecteds(List<Object> rejecteds) {
        this.rejecteds = rejecteds;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public ClientRating getClientRating() {
        return clientRating;
    }

    public void setClientRating(ClientRating clientRating) {
        this.clientRating = clientRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<List<Float>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Float>> points) {
        this.points = points;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @SerializedName("_price")
    @Expose
    private Long price;
}
