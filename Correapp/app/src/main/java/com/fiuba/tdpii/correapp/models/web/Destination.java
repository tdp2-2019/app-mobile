package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Destination {

    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @SerializedName("name")
    @Expose
    private String nombre;
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