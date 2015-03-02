package com.androidfu.nowplaying.app.api;

import com.androidfu.nowplaying.app.BuildConfig;
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

    public RestClient(String endpoint, boolean enableLogging) {

        GsonConverter jsonParserConverter = new GsonConverter(buildJsonParser(), "UTF-8");
        
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat("yyyy-MM-dd")
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(BuildConfig.DEBUG && enableLogging ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setEndpoint(endpoint)
                .setConverter(jsonParserConverter)
                //.setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new SessionRequestInterceptor())
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public static Gson buildJsonParser() {
        GsonBuilder jsonParserBuilder = new GsonBuilder();
        jsonParserBuilder.setPrettyPrinting();
        jsonParserBuilder.excludeFieldsWithoutExposeAnnotation();
        return jsonParserBuilder.create();
    }
    
    public ApiService getApiService() {
        return apiService;
    }

}
