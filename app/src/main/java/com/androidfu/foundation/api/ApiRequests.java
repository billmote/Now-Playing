package com.androidfu.foundation.api;

import com.androidfu.foundation.model.ApplicationSettings;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by billmote on 9/7/14.
 */
public interface APIRequests {
    final static String APPSETTINGS = "/ApplicationSettings.json";

    // APLICATION SETTINGS
    @GET(APPSETTINGS)
    void getApplicationSettings(Callback<ApplicationSettings> cb);

}
