package com.fiuba.tdpii.correapp.networking;

import com.fiuba.tdpii.correapp.models.web.PutTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.SerializedTrips;
import com.fiuba.tdpii.correapp.models.web.TripPost;
import com.fiuba.tdpii.correapp.models.web.TripPutRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface CorreappApi {

    @POST("/trips")
    Call<SerializedTripPostResponse> createTrip(@Header("Content-Type") String content_type, @Body TripPost tripSerialized);

    @GET("/trips/{id}")
    Call<SerializedTripPostResponse> getTripById(@Path("id") String id);

    @GET("/trips")
    Call<List<SerializedTripPostResponse>> getTrips();

    @PUT("/trips/{id}")
    Call<SerializedTripPostResponse> putDriver(@Path("id") String id, @Body TripPutRequest tripPut);



//        @POST("/personal_medicine_reminder")
//        Call<Void> createPill(@Header("Authorization") String accessToken, @Header("Content-Type") String content_type, @Body PillSerialized pillSerialized);
//
//        @GET("/personal_medicine_reminder")
//        Call<ArrayList<PillResponse>> getPillsForToday(@Header("Authorization") String accessToken);
//
//        //TODO
//        @PUT("/personal_medicine_reminder")
//        Call<Void> drinkedPill(@Header("Authorization") String accessToken, @Path("id") String id);
//
//        @POST("/users/sessions")
//        Call<UserSession> createUserSession(@Body UserSessionRequest sessionRequest);

}

