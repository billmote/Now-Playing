package com.androidfu.foundation.api;

import android.content.Context;

import com.androidfu.foundation.events.APIOkEvent;
import com.androidfu.foundation.events.RetrieveApplicationSettingsEvent;
import com.androidfu.foundation.localcache.ApplicationSettingsLocalStorageHandler;
import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.util.EventBus;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;

import hugo.weaving.DebugLog;
import retrofit.client.Response;

/**
 * Created by billmote on 9/7/14.
 */
public class ApiEventHandler {
    private ApiRequests mApiRequests;
    private final ApplicationSettingsLocalStorageHandler mApplicationSettingsLocalStorageHandler;
    private final Context mContext;

    @DebugLog
    public ApiEventHandler(Context context) {
        mApplicationSettingsLocalStorageHandler = new ApplicationSettingsLocalStorageHandler(context);
        mApiRequests = ApiBuilder.createApiInstance(context);
        mContext = context;
    }

    /**
     * APPLICATION SETTINGS
     */
    @DebugLog
    @Subscribe
    public void getApplicationSettings(final RetrieveApplicationSettingsEvent event) {
        this.mApiRequests.getApplicationSettings(new ApiHandler<ApplicationSettings>(event.getCallNumber()) {
            @Override
            public void success(ApplicationSettings applicationSettings, Response response) {
                try {
                    ApiEventHandler.this.mApplicationSettingsLocalStorageHandler.saveCurrentApplicationSettings(applicationSettings);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                EventBus.post(new APIOkEvent(event.getCallNumber()));
            }
        });
    }
}