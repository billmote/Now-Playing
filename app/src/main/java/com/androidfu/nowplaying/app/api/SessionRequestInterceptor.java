package com.androidfu.nowplaying.app.api;

import retrofit.RequestInterceptor;

/**
 * Created by billmote on 2/22/15.
 */
public class SessionRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
        //if (user is connected){
        //    request.addHeader("Header name", "Header Value");
        //}
    }
}