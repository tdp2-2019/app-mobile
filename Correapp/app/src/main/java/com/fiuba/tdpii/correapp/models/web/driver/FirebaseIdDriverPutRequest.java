package com.fiuba.tdpii.correapp.models.web.driver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirebaseIdDriverPutRequest {


    @SerializedName("firebase_id")
    @Expose
    private String firebaseId;

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

}
