package com.fiuba.tdpii.correapp.services.trips;

import android.content.Context;

public interface TripClient<T> {

    public abstract void onResponseSuccess(T responseBody);

    public abstract void onResponseError();

    public abstract Context getApplicationContext();

}
