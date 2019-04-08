package com.fiuba.tdpii.correapp.models.web;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripSerialized {

    @Expose
    @SerializedName("trip")
    public Personal_medicine_reminder personal_medicine_reminder;

    public static class Personal_medicine_reminder {
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("notes")
        public String notes;
        @Expose
        @SerializedName("date")
        public String date;
        @Expose
        @SerializedName("time")
        public String time;
    }


}
