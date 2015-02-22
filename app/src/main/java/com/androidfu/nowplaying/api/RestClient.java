package com.androidfu.nowplaying.api;

import android.content.Context;

import com.androidfu.nowplaying.BuildConfig;
import com.androidfu.nowplaying.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hugo.weaving.DebugLog;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class RestClient {

    private ApiService apiService;

    public RestClient(Context context, String endpoint) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat("yyyy-MM-dd")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(BuildConfig.DEBUG && Boolean.valueOf(context.getResources().getString(R.string.retrofit_logging_enabled)) ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(gson, "UTF-8"))
                .setRequestInterceptor(new SessionRequestInterceptor())
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

    public static Gson buildJsonParser() {
        GsonBuilder jsonParserBuilder = new GsonBuilder();
        jsonParserBuilder.setPrettyPrinting();
        jsonParserBuilder.excludeFieldsWithoutExposeAnnotation();
        return jsonParserBuilder.create();
    }
}
