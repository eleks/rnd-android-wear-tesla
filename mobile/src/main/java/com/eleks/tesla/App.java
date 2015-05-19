package com.eleks.tesla;

import android.app.Application;

import com.eleks.tesla.api.TeslaApi;

/**
 * Created by bogdan.melnychuk on 20.05.2015.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TeslaApi.init(this.getDir("tesla_api", 0));
    }
}
