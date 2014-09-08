package com.androidfu.foundation.api;

import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.model.QuoteOfTheDay;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by billmote on 9/7/14.
 */
public interface APIRequests {
    final static String APPSETTINGS = "/ApplicationSettings.json";
    final static String QUOTE_OF_THE_DAY = "/random?format=json&from=subversion";

    // APLICATION SETTINGS
    @GET(APPSETTINGS)
    void getApplicationSettings(Callback<ApplicationSettings> cb);

    // QUOTE OF THE DAY
    @GET(QUOTE_OF_THE_DAY)
    void getQuoteOfTheDay(Callback<QuoteOfTheDay> cb);
}
