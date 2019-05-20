package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripPositionPutRequest {


    public Destination getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Destination currentPosition) {
        this.currentPosition = currentPosition;
    }

    @SerializedName("currentposition")
    @Expose
    private Destination currentPosition;


}
