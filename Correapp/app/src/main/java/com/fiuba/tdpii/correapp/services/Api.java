package com.fiuba.tdpii.correapp.services;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private final static String API_BASE_URL = "https://correapp-api.herokuapp.com/";

    private static Api instance;
    private Retrofit retrofit;


    private Api(Context applicationContext) {
        buildRetrofit(applicationContext);
    }

    public synchronized static Api getInstance(Context applicationContext) {
        if (instance == null)
            instance = new Api(applicationContext);
        return instance;
    }

    private void buildRetrofit(Context applicationContext) {

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(applicationContext.getCacheDir(), cacheSize);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().cache(cache);

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(gsonConverterFactory);

        retrofit = builder
                .client(httpClient.build())
                .build();
    }

    public ApiInterface getClient() {
        return retrofit.create(ApiInterface.class);
    }
}
