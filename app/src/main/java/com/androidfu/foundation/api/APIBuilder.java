package com.androidfu.foundation.api;

import android.content.Context;
import android.provider.Settings;

import com.androidfu.foundation.BuildConfig;
import com.androidfu.foundation.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hugo.weaving.DebugLog;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by billmote on 9/7/14.
 */
public class APIBuilder {
    public static final String AUTH_TOKEN_HEADER = "Auth-Token";
    public static final String ANDROIDID_HEADER = "Device-ID";

    private static String currentAuthToken;
    private static String currentAndroidId;

    private static APIRequests api;

    @DebugLog
    public static APIRequests getApiInstance() {
        return api;
    }

    @DebugLog
    public static APIRequests createApiInstance(Context context, String endpoint) {
        return createApiInstance(context, "", endpoint);
    }

    @DebugLog
    public static APIRequests createApiInstance(Context context, final String token, String endpoint) {
        final String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        GsonConverter jsonParserConverter = new GsonConverter(buildJsonParser(), "UTF-8");

        currentAndroidId = androidID;
        currentAuthToken = token;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setConverter(jsonParserConverter)
                .setLogLevel(BuildConfig.DEBUG && Boolean.valueOf(context.getResources().getString(R.string.retrofit_logging_enabled)) ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade requestFacade) {
                        requestFacade.addHeader(AUTH_TOKEN_HEADER, token);
                        requestFacade.addHeader(ANDROIDID_HEADER, androidID);
                    }
                })
                .build();

        api = restAdapter.create(APIRequests.class);
        return getApiInstance();
    }

    @DebugLog
    public static Gson buildJsonParser() {
        GsonBuilder jsonParserBuilder = new GsonBuilder();
        jsonParserBuilder.setPrettyPrinting();
        jsonParserBuilder.excludeFieldsWithoutExposeAnnotation();
        return jsonParserBuilder.create();
    }
}
