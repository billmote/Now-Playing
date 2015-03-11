package com.androidfu.nowplaying.app.api;

import android.content.Context;
import android.widget.Toast;

import com.androidfu.nowplaying.app.R;
import com.androidfu.nowplaying.app.events.APIErrorEvent;
import com.androidfu.nowplaying.app.events.APIOkEvent;
import com.androidfu.nowplaying.app.events.application.GetApplicationSettingsEvent;
import com.androidfu.nowplaying.app.events.movies.GetMoviesEvent;
import com.androidfu.nowplaying.app.localcache.AppSettingsLocalStorageHandler;
import com.androidfu.nowplaying.app.model.application.ApplicationSettings;
import com.androidfu.nowplaying.app.model.movies.Movies;
import com.androidfu.nowplaying.app.util.EventBus;
import com.squareup.otto.Subscribe;

import org.apache.http.HttpException;

import java.sql.SQLException;

import hugo.weaving.DebugLog;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * This class is registered with our EventBus in NowPlayingApplication class and will be responsible
 * for responding to all events for which we wish to fetch information from the internet.
 * <p/>
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class ServiceEventHandler {

    private final AppSettingsLocalStorageHandler mApplicationSettingsLocalStorageHandler;
    private boolean isRetrofitLoggingEnabled;
    private String appSettingsUrl;
    private String rottenTomatoesUrl;
    private String rottenTomatoesApiKey;

    public ServiceEventHandler(Context context) {
        mApplicationSettingsLocalStorageHandler = new AppSettingsLocalStorageHandler(context);
        isRetrofitLoggingEnabled = Boolean.valueOf(context.getString(R.string.retrofit_logging_enabled));
        appSettingsUrl = context.getString(R.string.application_settings_url);
        rottenTomatoesUrl = context.getString(R.string.movies_url);
        rottenTomatoesApiKey = context.getString(R.string.rotten_tomatoes_api_key); /* Look in res/values/secret.xml */
        if (rottenTomatoesApiKey.equals("REPLACE WITH YOUR KEY")) {
            Toast.makeText(context, context.getString(R.string.error_replace_api_key), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * APPLICATION SETTINGS
     */
    @Subscribe
    public void getApplicationSettings(final GetApplicationSettingsEvent event) {
        ApiService apiService = new RestClient(appSettingsUrl, isRetrofitLoggingEnabled).getApiService();
        apiService.getApplicationSettings(new RestCallback<ApplicationSettings>(event.getCallNumber()) {
            @Override
            public void success(ApplicationSettings applicationSettings, Response response) {
                try {
                    ServiceEventHandler.this.mApplicationSettingsLocalStorageHandler.saveCurrentApplicationSettings(applicationSettings);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                EventBus.post(new APIOkEvent(event.getCallNumber()));
            }
        });
    }

    /**
     * 'IN THEATER' MOVIES
     */
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
        ApiService apiService = new RestClient(rottenTomatoesUrl, isRetrofitLoggingEnabled).getApiService();
        apiService.getMovies(rottenTomatoesApiKey, event.getPageNumber(), event.getPageLimit(), new RestCallback<Movies>(event.getCallNumber()) {
            @Override
            public void success(Movies movies, Response response) {
                if (movies != null && !movies.getMovieList().isEmpty()) {
                    EventBus.post(movies);
                } else {
                    EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
                }
            }
        });
    }
}
