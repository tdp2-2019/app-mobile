package com.fiuba.tdpii.correapp.services.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DestinationResponse {

    @SerializedName("lat")
    private String lat;

    @SerializedName("long")
    private String _long;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

}
