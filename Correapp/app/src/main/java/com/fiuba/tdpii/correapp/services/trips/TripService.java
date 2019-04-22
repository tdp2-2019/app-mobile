package com.fiuba.tdpii.correapp.services.trips;

import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.StartTripPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripClientRatingRequest;
import com.fiuba.tdpii.correapp.models.web.TripDriverRatingRequest;
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
