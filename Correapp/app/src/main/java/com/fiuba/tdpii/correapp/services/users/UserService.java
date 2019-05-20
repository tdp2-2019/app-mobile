package com.fiuba.tdpii.correapp.services.users;

import com.fiuba.tdpii.correapp.models.web.DriversByTrip;
import com.fiuba.tdpii.correapp.models.web.Rejected;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.StartTripPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripClientRatingRequest;
import com.fiuba.tdpii.correapp.models.web.TripDriverRatingRequest;
import com.fiuba.tdpii.correapp.models.web.TripPost;
import com.fiuba.tdpii.correapp.models.web.TripPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripRejectionRequest;
import com.fiuba.tdpii.correapp.models.web.user.ClientResponse;
import com.fiuba.tdpii.correapp.networking.ApiClient;
import com.fiuba.tdpii.correapp.networking.CorreappApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class UserService {

    private CorreappApi coreAPI;

    public UserService() {
        coreAPI = ApiClient.getInstance().getCorreapClient();

    }


    public Call<ClientResponse> getUserById(String clientId){
        return coreAPI.getUserById(clientId);
    }

    public Call<List<ClientResponse>> getUsersByEmail( String email){
        return coreAPI.getUsersByEmail(email);
    }

    public Call<ClientResponse> saveNewUser(ClientResponse client) {
        return coreAPI.createUser("application/json", client);
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


//    public Call<ArrayList<TripResponse>> getTrips(final ApiClient delegate){
//        coreAPI.getTrips().enqueue(new Callback<ArrayList<TripResponse>>() {
//            @Override
//            public void onResponse(Call<ArrayList<TripResponse>> call, Response<ArrayList<TripResponse>> response) {
//                if (response.code() > 199 && response.code() < 300) {
//                    if(response.body() != null) {
//                        Log.i("BOOKSERVICE", response.body().toString());
//                        delegate.onResponseSuccess(response.body());
//                    }else {
//                        Log.i("BOOKSERVICE", "NO RESPONSE");
//                        delegate.onResponseError();
//                    }
//                } else {
//                    if(response.body() != null) {
//                        Log.e("BOOKSERVICE", response.body().toString());
//                    }else {
//                        Log.e("BOOKSERVICE", "NO RESPONSE");
//                    }
//                    delegate.onResponseError();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<TripResponse>> call, Throwable t) {
//                delegate.onResponseError();
//                Log.e("BOOKSERVICE", t.getMessage());
//            }
//        });
//
//    }
}
