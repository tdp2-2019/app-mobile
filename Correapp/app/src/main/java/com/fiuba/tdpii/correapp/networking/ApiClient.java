package com.fiuba.tdpii.correapp.networking;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private final static String API_BASE_URL = "https://correapp-api.herokuapp.com/";
    private static ApiClient instance;
    private Retrofit retrofit;

    private ApiClient() {
        buildRetrofit();
    }

    public synchronized static ApiClient getInstance() {
        if (instance == null)
            instance = new ApiClient();
        return instance;
    }

    private void buildRetrofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder
                .client(httpClient.build())
                .build();
    }

    public CorreappApi getCorreapClient() {
        return retrofit.create(CorreappApi.class);
    }
}
