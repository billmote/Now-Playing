package com.androidfu.foundation.api;

import android.content.Context;

import com.androidfu.foundation.events.APIOkEvent;
import com.androidfu.foundation.events.GetApplicationSettingsEvent;
import com.androidfu.foundation.localcache.AppSettingsLocalStorageHandler;
import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.util.EventBus;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;

import hugo.weaving.DebugLog;
import retrofit.client.Response;

/**
 * Created by billmote on 9/7/14.
 */
public class APIEventHandler {
    private APIRequests mAPIRequests;
    private final AppSettingsLocalStorageHandler mApplicationSettingsLocalStorageHandler;
    private final Context mContext;

    @DebugLog
    public APIEventHandler(Context context) {
        mApplicationSettingsLocalStorageHandler = new AppSettingsLocalStorageHandler(context);
        mAPIRequests = APIBuilder.createApiInstance(context);
        mContext = context;
    }

    /**
     * APPLICATION SETTINGS
     */
    @DebugLog
    @Subscribe
    public void getApplicationSettings(final GetApplicationSettingsEvent event) {
        this.mAPIRequests.getApplicationSettings(new APIHandler<ApplicationSettings>(event.getCallNumber()) {
            @Override
            public void success(ApplicationSettings applicationSettings, Response response) {
                try {
                    APIEventHandler.this.mApplicationSettingsLocalStorageHandler.saveCurrentApplicationSettings(applicationSettings);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                EventBus.post(new APIOkEvent(event.getCallNumber()));
            }
        });
    }
}