package com.fiuba.tdpii.correapp.services;

import com.fiuba.tdpii.correapp.services.responses.DriverRatingResponse;
import com.fiuba.tdpii.correapp.services.responses.TripResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @PUT("/trips/{id}")
    Call<TripResponse> rateTripDriver(@Path("id") int tripId, @Body DriverRatingResponse rating);
}
