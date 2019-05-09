package com.fiuba.tdpii.correapp.models.web;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;

public class SerializedTripPostResponse implements Comparable<SerializedTripPostResponse> {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("companion")
    @Expose
    private Boolean companion;
    @SerializedName("source")
    @Expose
    private Destination source;
    @SerializedName("destination")
    @Expose
    private Destination destination;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private Object endTime;
    @SerializedName("rejecteds")
    @Expose
    private List<Long> rejecteds = null;
    @SerializedName("pets")
    @Expose
    private List<Pet> pets = null;
    @SerializedName("driver_rating")
    @Expose
    private Rating driverRating;
    @SerializedName("user_rating")
    @Expose
    private Rating userRating;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("driver_id")
    @Expose
    private Long driverId;
    @SerializedName("user_id")
    @Expose
    private Long userId;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("points")
    @Expose
    private List<List<Float>> points = null;
    @SerializedName("duration")
    @Expose
    private Long duration;
    @SerializedName("client")
    @Expose
    private String client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCompanion() {
        return companion;
    }

    public void setCompanion(Boolean companion) {
        this.companion = companion;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public List<Long> getRejecteds() {
        return rejecteds;
    }

    public void setRejecteds(List<Long> rejecteds) {
        this.rejecteds = rejecteds;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public Rating getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(Rating driverRating) {
        this.driverRating = driverRating;
    }

    public Rating getUserRating() {
        return userRating;
    }

    public void setUserRating(Rating userRating) {
        this.userRating = userRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public int compareTo(@NonNull SerializedTripPostResponse o) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        try {
            Date oDate = dateFormat.parse(o.getStartTime());
            Date myDate = dateFormat.parse(this.getStartTime());
            return oDate.compareTo(myDate);
        } catch (ParseException e) {
            return 1;
        }
    }
}