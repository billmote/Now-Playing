package com.androidfu.nowplaying.api;

import com.androidfu.nowplaying.model.application.ApplicationSettings;
import com.androidfu.nowplaying.model.movies.Movies;

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

    // QUOTE OF THE DAY
    @GET(MOVIES)
    void getMovies(@Query("apikey") String key, @Query("page") int page, @Query("page_limit") int page_limit, Callback<Movies> cb);
}
