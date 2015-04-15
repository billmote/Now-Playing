package com.androidfu.nowplaying.app.api;

import com.androidfu.nowplaying.app.model.application.ApplicationSettings;
import com.androidfu.nowplaying.app.model.movies.Movies;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public interface ApiService {
    final static String APPSETTINGS = "/application_settings.json";
    final static String MOVIES = "/in_theaters.json";

    // APPLICATION SETTINGS
    @GET(APPSETTINGS)
    void getApplicationSettings(Callback<ApplicationSettings> cb);

    // IN THEATERS
    @GET(MOVIES)
    void getMovies(@Query("apikey") String key, @Query("page") int page, @Query("page_limit") int page_limit, Callback<Movies> cb);
}
