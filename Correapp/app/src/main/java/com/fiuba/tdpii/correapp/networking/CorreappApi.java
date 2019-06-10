package com.fiuba.tdpii.correapp.networking;

import com.fiuba.tdpii.correapp.models.web.AbortTripRequest;
import com.fiuba.tdpii.correapp.models.web.DriversByTrip;
import com.fiuba.tdpii.correapp.models.web.PutTrip;
import com.fiuba.tdpii.correapp.models.web.Rejected;
import com.fiuba.tdpii.correapp.models.web.SerializedTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.SerializedTrips;
import com.fiuba.tdpii.correapp.models.web.StartTripPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripClientRatingRequest;
import com.fiuba.tdpii.correapp.models.web.TripDriverRatingRequest;
import com.fiuba.tdpii.correapp.models.web.TripPositionPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripPost;
import com.fiuba.tdpii.correapp.models.web.TripPutClientRatingRequest;
import com.fiuba.tdpii.correapp.models.web.TripPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripRejectionRequest;
import com.fiuba.tdpii.correapp.models.web.driver.DriverPost;
import com.fiuba.tdpii.correapp.models.web.driver.FirebaseIdDriverPutRequest;
import com.fiuba.tdpii.correapp.models.web.user.ClientResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface CorreappApi {

    @POST("/trips")
    Call<SerializedTripPostResponse> createTrip(@Header("Content-Type") String content_type, @Body TripPost tripSerialized);

    @GET("/trips/{id}")
    Call<SerializedTripPostResponse> getTripById(@Path("id") String id);

    @GET("/trips")
    Call<List<SerializedTripPostResponse>> getTrips();

    @PUT("/trips/{id}")
    Call<SerializedTripPostResponse> putDriver(@Path("id") String id, @Body TripPutRequest tripPut);


    @POST("/drivers")
    Call<DriverPost> createDriver(@Header("Content-Type") String content_type, @Body DriverPost driverSerializedPost);


    @GET("/drivers/{id}")
    Call<DriverPost> getDriverById(@Path("id") String id);

    @PUT("/trips/{id}")
    Call<SerializedTripPostResponse> putRejection(@Path("id") String tripId,  @Body TripRejectionRequest body);

    @PUT("/trips/{id}")
    Call<SerializedTripPostResponse> putStatus(@Path("id") String tripId,  @Body StartTripPutRequest body);

    @PUT("/trips/{id}")
    Call<SerializedTripPostResponse> putDriverRating(@Path("id") String tripId, @Body TripDriverRatingRequest body);

    @PUT("/trips/{id}")
    Call<SerializedTripPostResponse> putClientRating(@Path("id") String tripId, @Body TripClientRatingRequest body);

    @GET("/drivers")
    Call<List<DriverPost>> getDriversByEmail(@Query("email") String email);

    @GET("/trips")
    Call<List<SerializedTripPostResponse>> getTripsDoneByDriver(@Query("driver_id") String driverId, @Query("status") String accepted);

    @GET("/trips/{id}/rejects")
    Call<List<Rejected>> getRejectedByTrips(@Path("id") String tripId);

    @GET("/trips/{id}/drivers")
    Call<List<DriversByTrip>> getDriversByTrip(@Path("id") String tripId);

    @PUT("/drivers/{id}")
    Call<DriverPost> putFirebaseIdById(@Path("id") String driverId, @Body FirebaseIdDriverPutRequest request);

    @GET("/users/{id}")
    Call<ClientResponse> getUserById(@Path("id") String clientId);

    @GET("/users")
    Call<List<ClientResponse>> getUsersByEmail(@Query("email") String email);

    @POST("/users")
    Call<ClientResponse> createUser(@Header("Content-Type") String content_type, @Body ClientResponse client);

    @PUT("/trips/{id}")
    Call<SerializedTripPostResponse> putTripPosition(@Path("id") String tripId, @Body TripPositionPutRequest body);

    @PUT("/trips/{id}")
    Call<SerializedTripPostResponse> putAbort(@Path("id") String tripId, @Body AbortTripRequest body);
}

