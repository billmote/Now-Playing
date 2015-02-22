package com.androidfu.nowplaying.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hugo.weaving.DebugLog;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class APIBuilder {

    public static APIRequests createApiInstance(Context context, String endpoint) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(buildJsonParser(), "UTF-8"))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                        //.setLogLevel(BuildConfig.DEBUG && Boolean.valueOf(context.getResources().getString(R.string.retrofit_logging_enabled)) ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
        return restAdapter.create(APIRequests.class);
    }

    public static Gson buildJsonParser() {
        GsonBuilder jsonParserBuilder = new GsonBuilder();
        jsonParserBuilder.setPrettyPrinting();
        jsonParserBuilder.excludeFieldsWithoutExposeAnnotation();
        return jsonParserBuilder.create();
    }
}
