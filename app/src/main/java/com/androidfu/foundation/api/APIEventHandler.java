package com.androidfu.foundation.api;

import android.content.Context;

import com.androidfu.foundation.R;
import com.androidfu.foundation.events.APIErrorEvent;
import com.androidfu.foundation.events.APIOkEvent;
import com.androidfu.foundation.events.GetApplicationSettingsEvent;
import com.androidfu.foundation.events.GetMoviesEvent;
import com.androidfu.foundation.localcache.AppSettingsLocalStorageHandler;
import com.androidfu.foundation.model.ApplicationSettings;
import com.androidfu.foundation.model.Movies;
import com.androidfu.foundation.util.EventBus;
import com.squareup.otto.Subscribe;

import org.apache.http.HttpException;

import java.sql.SQLException;

import hugo.weaving.DebugLog;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by billmote on 9/7/14.
 */
public class APIEventHandler {
    //private APIRequests mAPIRequests;
    private final AppSettingsLocalStorageHandler mApplicationSettingsLocalStorageHandler;
    private final Context mContext;

    @DebugLog
    public APIEventHandler(Context context) {
        mApplicationSettingsLocalStorageHandler = new AppSettingsLocalStorageHandler(context);
        //mAPIRequests = APIBuilder.createApiInstance(context);
        mContext = context;
    }

    /**
     * APPLICATION SETTINGS
     */
    @DebugLog
    @Subscribe
    public void getApplicationSettings(final GetApplicationSettingsEvent event) {
        APIBuilder.createApiInstance(mContext, mContext.getString(R.string.application_settings_url)).getApplicationSettings(new APIHandler<ApplicationSettings>(event.getCallNumber()) {
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

    /**
     * QUOTE OF THE DAY
     */
    @DebugLog
    @Subscribe
    public void getMovies(final GetMoviesEvent event) {
        /**
         * rotten_tomatoes_api_key is stored in a strings resource file named secret.xml and
         * located in res/values, but secret.xml has been added to .gitignore so my key doesn't
         * end up in source control.
         *
         * simply create your own res/values/secret.xml file with your own Rotten Tomatoes API
         * key and the app will run as expected.
         */
        APIBuilder.createApiInstance(mContext, mContext.getString(R.string.movies_url)).getMovies(mContext.getString(R.string.rotten_tomatoes_api_key), new APIHandler<Movies>(event.getCallNumber()) {
            @Override
            public void success(Movies movies, Response response) {
                if (response.getBody().length() > 0) {
                    EventBus.post(movies);
                } else {
                    EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(mContext.getString(R.string.movies_url), new HttpException("Empty Body")), event.getCallNumber()));
                }
            }
        });
    }
}