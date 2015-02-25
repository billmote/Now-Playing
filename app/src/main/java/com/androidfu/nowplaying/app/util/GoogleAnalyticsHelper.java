package com.androidfu.nowplaying.app.util;

import android.app.Application;

import com.androidfu.nowplaying.app.BuildConfig;
import com.androidfu.nowplaying.app.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/6/14.
 */
@DebugLog
public class GoogleAnalyticsHelper {

    private static final HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    private static GoogleAnalyticsHelper mInstance;
    private static GoogleAnalytics mGoogleAnalytics;
    private static Application mContext;

    private GoogleAnalyticsHelper() {
        mGoogleAnalytics = GoogleAnalytics.getInstance(mContext);
        mGoogleAnalytics.enableAutoActivityReports(mContext);
        mGoogleAnalytics.getLogger().setLogLevel(BuildConfig.DEBUG && Boolean.valueOf(mContext.getString(R.string.analytics_logging_enabled)) ? Logger.LogLevel.VERBOSE : Logger.LogLevel.ERROR);
        mGoogleAnalytics.setDryRun(BuildConfig.DEBUG);
    }

    public static void initialize(Application context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new GoogleAnalyticsHelper();
        }
    }

    public static GoogleAnalyticsHelper getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Call initialize() from your Application class with Application Context.");
        }
        return mInstance;
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            Tracker tracker = (trackerId == TrackerName.APP_TRACKER) ? mGoogleAnalytics.newTracker(R.xml.application_tracker)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? mGoogleAnalytics.newTracker(R.xml.global_tracker)
                    : mGoogleAnalytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, tracker);
        }
        return mTrackers.get(trackerId);
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }
}
