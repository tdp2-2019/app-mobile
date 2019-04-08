//package com.fiuba.tdpii.correapp.services.trips;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.fiuba.tdpii.correapp.models.web.TripResponse;
//import com.fiuba.tdpii.correapp.networking.ApiClient;
//import com.fiuba.tdpii.correapp.networking.CorreappApi;
//
//import java.util.ArrayList;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class TripService {
//
//    private CorreappApi coreAPI;
//
//    public TripService(Context applicationContext) {
//        coreAPI = ApiClient.getInstance().getCorreapClient();
//
//    }
//
////    public Call<ArrayList<BookResponse>> getBooks(String searchQuery) {
////        return coreAPI.getBooks(searchQuery);
////    }
//
//    public Call<TripResponse> getTripById(String id){
//        return coreAPI.getTripById(id);
//    }
//
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
//}
