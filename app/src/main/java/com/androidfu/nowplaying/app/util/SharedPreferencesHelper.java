package com.androidfu.nowplaying.app.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.androidfu.nowplaying.app.R;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/4/14.
 */
@DebugLog
public class SharedPreferencesHelper {
    public static final String TAG = "SharedPreferencesHelper";

    public static final String KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS = "last_seen_motd_time";
    public static final String KEY_PREFS_FIRST_RUN = "first_run";

    private static SharedPreferencesHelper mInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;
    private static Application mContext;

    private SharedPreferencesHelper() {
    }

    public static void initialize(Application context) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesHelper();
        }
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        mContext = context;
    }

    private static SharedPreferencesHelper getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(mContext.getString(R.string.error_preferences_illegal_state));
        }
        return mInstance;
    }

    public static void clear() {
        getInstance().sharedPreferencesEditor.clear().commit();
    }

    public static void putBoolean(String key, boolean b) {
        getInstance().sharedPreferencesEditor.putBoolean(key, b).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getInstance().sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void putStringPreference(String key, String s) {
        getInstance().sharedPreferencesEditor.putString(key, s).commit();
    }

    public static String getStringPreference(String key, String defaultValue) {
        return getInstance().sharedPreferences.getString(key, defaultValue);
    }

    public static void putLong(String key, long l) {
        getInstance().sharedPreferencesEditor.putLong(key, l).commit();
    }

    public static long getLong(String key, long defaultValue) {
        return getInstance().sharedPreferences.getLong(key, defaultValue);
    }

}
