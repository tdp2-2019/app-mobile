package com.fiuba.tdpii.correapp.services.trips;

import com.fiuba.tdpii.correapp.models.web.AbortTripRequest;
import com.fiuba.tdpii.correapp.models.web.DriversByTrip;
import com.fiuba.tdpii.correapp.models.web.Rejected;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.StartTripPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripClientRatingRequest;
import com.fiuba.tdpii.correapp.models.web.TripDriverRatingRequest;
import com.fiuba.tdpii.correapp.models.web.TripPositionPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripPost;
import com.fiuba.tdpii.correapp.models.web.TripPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripRejectionRequest;
import com.fiuba.tdpii.correapp.networking.ApiClient;
import com.fiuba.tdpii.correapp.networking.CorreappApi;

import java.util.List;

import retrofit2.Call;

public class TripService {

    private CorreappApi coreAPI;

    public TripService() {
        coreAPI = ApiClient.getInstance().getCorreapClient();

    }

    public Call<SerializedTripPostResponse> abortTrip(AbortTripRequest body, String tripId) {
        return coreAPI.putAbort(tripId, body);
    }

    public Call<List<SerializedTripPostResponse>> getTrips() {
        return coreAPI.getTrips();
    }


    public Call<SerializedTripPostResponse> getTripById(String id) {

        return coreAPI.getTripById(id);

    }

    public Call<SerializedTripPostResponse> saveNewTrip(TripPost trip) {
        return coreAPI.createTrip("application/json", trip);
    }

    public Call<SerializedTripPostResponse> updateDriver(TripPutRequest body, String tripId) {
        return coreAPI.putDriver(tripId, body);
    }

    public Call<SerializedTripPostResponse> startTrip(StartTripPutRequest body, String tripId) {
        return coreAPI.putStatus(tripId, body);
    }

    public Call<SerializedTripPostResponse> rejectTrip(TripRejectionRequest body, String tripId) {
        return coreAPI.putRejection(tripId, body);
    }

    public Call<SerializedTripPostResponse> rateDriver(TripDriverRatingRequest body, String tripId) {
        return coreAPI.putDriverRating(tripId, body);
    }

    public Call<SerializedTripPostResponse> rateClient(TripClientRatingRequest body, String tripId) {
        return coreAPI.putClientRating(tripId, body);
    }

    public Call<List<SerializedTripPostResponse>> getTripsDoneByDriver(String driverId) {

        return coreAPI.getTripsDoneByDriver(driverId, "finished");

    }

    public Call<List<Rejected>> getRejectedsByTrip(String tripId) {

        return coreAPI.getRejectedByTrips(tripId);
    }

    public Call<List<DriversByTrip>> getDriversByTrip(String tripId) {

        return coreAPI.getDriversByTrip(tripId);
    }


    public Call<SerializedTripPostResponse> updatePosition(TripPositionPutRequest body, String tripId) {
        return coreAPI.putTripPosition(tripId, body);
    }

}
