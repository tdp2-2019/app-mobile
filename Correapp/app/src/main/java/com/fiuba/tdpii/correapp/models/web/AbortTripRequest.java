package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbortTripRequest {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("price")
    @Expose
    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
