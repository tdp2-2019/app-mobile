package com.fiuba.tdpii.correapp.services.drivers;

import android.content.Context;

public interface DriverClient<T> {

    public abstract void onResponseSuccess(T responseBody);

    public abstract void onResponseError();

    public abstract Context getApplicationContext();

}
