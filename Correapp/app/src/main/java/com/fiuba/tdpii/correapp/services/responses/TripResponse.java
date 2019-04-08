package com.fiuba.tdpii.correapp.services.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TripResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("current_position")
    private List<Double> currentPosition = null;

    @SerializedName("client")
    private String client;

    @SerializedName("destination")
    private DestinationResponse destination;

    @SerializedName("duration")
    private Integer duration;

    @SerializedName("pets")
    private String pets;

    @SerializedName("price")
    private Integer price;

    @SerializedName("driver_rating")
    private DriverRatingResponse driverRating;

    @SerializedName("client_rating")
    private ClientRatingResponse clientRating;

    @SerializedName("driver_id")
    private Object driverId;

    @SerializedName("start_time")
    private Integer startTime;

    @SerializedName("end_time")
    private Object endTime;

    @SerializedName("rejecteds")
    private List<Object> rejecteds = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Double> getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(List<Double> currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public DestinationResponse getDestination() {
        return destination;
    }

    public void setDestination(DestinationResponse destination) {
        this.destination = destination;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public DriverRatingResponse getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(DriverRatingResponse driverRating) {
        this.driverRating = driverRating;
    }

    public ClientRatingResponse getClientRating() {
        return clientRating;
    }

    public void setClientRating(ClientRatingResponse clientRating) {
        this.clientRating = clientRating;
    }

    public Object getDriverId() {
        return driverId;
    }

    public void setDriverId(Object driverId) {
        this.driverId = driverId;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
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
