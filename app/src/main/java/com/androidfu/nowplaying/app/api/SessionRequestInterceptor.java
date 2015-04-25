package com.androidfu.nowplaying.app.api;

import android.text.format.DateUtils;

import retrofit.RequestInterceptor;

/**
 * Created by billmote on 2/22/15.
 */
public class SessionRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {

        /*
            "max-stale" is the maximum amount of time the client will accept stale data while the server
            is trying to update its cache.
         */
        long maxStaleTime = DateUtils.HOUR_IN_MILLIS / 1000;
        request.addHeader("Cache-Control", "public, max-stale=" + maxStaleTime);

        /*
            "max-age" is the maximum age of data allowed by the cache before it will force a refresh.
         */
        long maxAgeTime = DateUtils.HOUR_IN_MILLIS / 1000;
        request.addHeader("Cache-Control", "public, max-age=" + maxAgeTime);
    }
}