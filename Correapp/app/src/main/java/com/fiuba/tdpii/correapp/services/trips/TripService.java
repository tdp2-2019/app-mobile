package com.fiuba.tdpii.correapp.services.trips;

import com.fiuba.tdpii.correapp.models.web.PutTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTrip;
import com.fiuba.tdpii.correapp.models.web.SerializedTripPostResponse;
import com.fiuba.tdpii.correapp.models.web.SerializedTrips;
import com.fiuba.tdpii.correapp.models.web.TripPost;
import com.fiuba.tdpii.correapp.models.web.TripPutRequest;
import com.fiuba.tdpii.correapp.models.web.TripResponse;
import com.fiuba.tdpii.correapp.models.web.TripSerialized;
import com.fiuba.tdpii.correapp.networking.ApiClient;
import com.fiuba.tdpii.correapp.networking.CorreappApi;

import retrofit2.Call;

public class TripService {

    private CorreappApi coreAPI;

    public TripService() {
        coreAPI = ApiClient.getInstance().getCorreapClient();

    }

    public Call<SerializedTrips> getTrips() {
//        coreAPI.getTrips().enqueue(new Callback<ArrayList<TripSerialized>>() {
//            @Override
//            public void onResponse(Call<ArrayList<TripSerialized>> call, Response<ArrayList<TripSerialized>> response) {
//                if (response.code() > 199 && response.code() < 300) {
//                    if (response.body() != null) {
//                        Log.i("TRIPSERVICE", response.body().toString());
//                        delegate.onResponseSuccess(response.body());
//                    } else {
//                        Log.i("TRIPSERVICE", "NO RESPONSE");
//                        delegate.onResponseError();
//                    }
//                } else {
//                    if (response.body() != null) {
//                        Log.e("TRIPSERVICE", response.body().toString());
//                    } else {
//                        Log.e("TRIPSERVICE", "NO RESPONSE");
//                    }
//                    delegate.onResponseError();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<TripSerialized>> call, Throwable t) {
//                delegate.onResponseError();
//                Log.e("TRIPSERVICE", t.getMessage());
//            }
//        });

            return coreAPI.getTrips();

    }


    public Call<SerializedTrip> getTripById(String id) {

        return coreAPI.getTripById(id);

    }

        public Call<SerializedTripPostResponse> saveNewTrip (TripPost trip){
            return coreAPI.createTrip("application/json", trip);
        }

        public Call<PutTrip> updateDriver(TripPutRequest body, String tripId){
            return coreAPI.putDriver(tripId, body );
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
