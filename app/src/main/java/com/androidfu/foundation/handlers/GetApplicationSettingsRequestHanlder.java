package com.androidfu.foundation.handlers;

import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.squareup.otto.Subscribe;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;

/**
 * Created by Bill on 8/1/14.
 */
public class GetApplicationSettingsRequestHanlder {

    private static final String TAG = GetApplicationSettingsRequestHanlder.class.getSimpleName();

    public interface ApplicationSettingsGetter {

        @DebugLog
        @GET("/")
        void someOtherResponse(Callback<ApplicationSettings> callBacks);

    }

    @DebugLog
    @Subscribe
    public void handleApplicationSettingsRequest(GetApplicationSettingsRequest request) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://catalog.data.gov/api/3")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ApplicationSettingsGetter getSettings = restAdapter.create(ApplicationSettingsGetter.class);

        getSettings.someOtherResponse(new Callback<ApplicationSettings>() {

                                          @Override
                                          public void success(ApplicationSettings applicationSettings, Response response) {
                                              Log.i(TAG, String.format("Object: %1$s", applicationSettings.toString()));
                                              Log.i(TAG, String.format("Response: %1$s", response));
                                              EventBus.post(applicationSettings);
                                          }

                                          @Override
                                          public void failure(RetrofitError retrofitError) {
                                              Log.i(TAG, "someOtherResponse failure callback.");
                                          }
                                      }
        );
    }

}
