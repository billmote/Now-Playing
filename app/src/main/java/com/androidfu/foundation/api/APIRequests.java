package com.androidfu.foundation.api;

import com.androidfu.foundation.model.application.ApplicationSettings;
import com.androidfu.foundation.model.movies.Movies;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by billmote on 9/7/14.
 */
public interface APIRequests {
    final static String APPSETTINGS = "/application_settings.json";
    final static String MOVIES = "/box_office.json";

    // APPLICATION SETTINGS
    @GET(APPSETTINGS)
    void getApplicationSettings(Callback<ApplicationSettings> cb);

    // QUOTE OF THE DAY
    @GET(MOVIES)
    void getMovies(@Query("apikey") String key, Callback<Movies> cb);
}
