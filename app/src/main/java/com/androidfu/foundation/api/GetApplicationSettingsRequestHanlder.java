package com.androidfu.foundation.api;

import android.net.Uri;
import android.text.format.DateUtils;

import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang.StringUtils;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.EncodedPath;
import retrofit.http.GET;

import static com.androidfu.foundation.FoundationApplication.*;
import static com.androidfu.foundation.util.SharedPreferencesHelper.*;

/**
 * Register your handler in the application's Application.class.
 * <p/>
 * Created by Bill on 8/1/14.
 */
public class GetApplicationSettingsRequestHanlder {

    private static final String TAG = GetApplicationSettingsRequestHanlder.class.getSimpleName();

    public interface GetApplicationSettings {

        @DebugLog
        @GET("/{file}")
        void get(@EncodedPath("file") String file, Callback<GetApplicationSettingsResponse> callBacks);

    }

    @DebugLog
    @Subscribe
    public void handleApplicationSettingsRequest(GetApplicationSettingsRequest request) {

        if (!getBoolean(KEY_PREFS_APP_IS_DISABLED_FLAG, false) && getLong(KEY_PREFS_LAST_FETCHED_TIME_IN_MILLIS, 0) > System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS) {
            /* If our app isn't disabled and we've already gotten our app settings today then bail out */
            EventBus.post(new ApplicationSettings());
            return;
        }

        // ALWAYS get app settings if our app is flagged as disabled as a precaution should the state change

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(StringUtils.remove(request.url, "/" + Uri.parse(request.url).getLastPathSegment()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        GetApplicationSettings getApplicationSettings = restAdapter.create(GetApplicationSettings.class);
        getApplicationSettings.get(Uri.parse(request.url).getLastPathSegment(), new Callback<GetApplicationSettingsResponse>() {

                    @DebugLog
                    @Override
                    public void success(GetApplicationSettingsResponse applicationSettingsResponse, Response response) {

                        // Save ourselves some typing
                        ApplicationSettings appSettings = applicationSettingsResponse.applicationSettings;

                        // Static import of SharedPreferencesHelper & FoundationApplication
                        putLong(KEY_PREFS_LAST_FETCHED_TIME_IN_MILLIS, System.currentTimeMillis());
                        putBoolean(KEY_PREFS_APP_IS_DISABLED_FLAG, appSettings.isAppDisabled);
                        putBoolean(KEY_PREFS_SHOW_UPDATE_NAG_FLAG, appSettings.isNagEnabled);
                        putBoolean(KEY_PREFS_UPDATE_AVAILABLE_FLAG, appSettings.productionVersionNumber > APP_VERSION_CODE);
                        putBoolean(KEY_PREFS_UPDATE_REQUIRED_FLAG, appSettings.lowWaterMarkVersionNumber > APP_VERSION_CODE);
                        putStringPreference(KEY_PREFS_KILLSWITCH_MESSAGE, appSettings.killswitchMessage);
                        putStringPreference(KEY_PREFS_MANDATORY_UPDATE_MESSAGE, appSettings.mandatoryUpdateMessage);
                        putStringPreference(KEY_PREFS_UPDATE_NAG_MESSAGE, appSettings.updateNagMessage);
                        putStringPreference(KEY_PREFS_MESSAGE_OF_THE_DAY, appSettings.messageOfTheDay);
                        putLong(KEY_PREFS_MOTD_LAST_UPDATED_TIME_IN_MILLIS, appSettings.updatedAt);
                        EventBus.post(appSettings);
                    }

                    @DebugLog
                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.e(TAG, retrofitError.getMessage());
                        EventBus.post(new ApplicationSettings());
                    }
                }
        );
    }

}
