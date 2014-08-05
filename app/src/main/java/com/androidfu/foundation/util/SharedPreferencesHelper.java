package com.androidfu.foundation.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.androidfu.foundation.R;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/4/14.
 */
public class SharedPreferencesHelper {
    public static final String TAG = SharedPreferencesHelper.class.getSimpleName();

    public static final String KEY_PREFS_LAST_FETCHED_TIME_IN_MILLIS = "last_app_settings_fetch_time";
    public static final String KEY_PREFS_UPDATE_AVAILABLE_FLAG = "update_available";
    public static final String KEY_PREFS_APP_IS_DISABLED_FLAG = "app_is_disabled_flag";
    public static final String KEY_PREFS_UPDATE_REQUIRED_FLAG = "update_required_flag";
    public static final String KEY_PREFS_SHOW_UPDATE_NAG_FLAG = "update_nag_flag";
    public static final String KEY_PREFS_KILLSWITCH_MESSAGE = "killswitch_message";
    public static final String KEY_PREFS_MANDATORY_UPDATE_MESSAGE = "mandatory_update_message";
    public static final String KEY_PREFS_UPDATE_NAG_MESSAGE = "update_nag_message";
    public static final String KEY_PREFS_MESSAGE_OF_THE_DAY = "message_of_the_day";
    public static final String KEY_PREFS_LAST_SEEN_MOTD_TIME_IN_MILLIS = "last_seen_motd_time";
    public static final String KEY_PREFS_MOTD_LAST_UPDATED_TIME_IN_MILLIS = "last_updated_motd_time";

    private static SharedPreferencesHelper mInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;
    private static Application mContext;

    @DebugLog
    private SharedPreferencesHelper() {

    }

    @DebugLog
    public static void initialize(Application context) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesHelper();
        }
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        mContext = context;
    }

    @DebugLog
    private static SharedPreferencesHelper getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(mContext.getString(R.string.error_preferences_illegal_state));
        }
        return mInstance;
    }

    @DebugLog
    public static void clear() {
        getInstance().sharedPreferencesEditor.clear().commit();
    }

    @DebugLog
    public static void putBoolean(String key, boolean b) {
        getInstance().sharedPreferencesEditor.putBoolean(key, b).commit();
    }

    @DebugLog

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getInstance().sharedPreferences.getBoolean(key, defaultValue);
    }

    @DebugLog
    public static void putStringPreference(String key, String s) {
        getInstance().sharedPreferencesEditor.putString(key, s).commit();
    }

    @DebugLog
    public static String getStringPreference(String key, String defaultValue) {
        return getInstance().sharedPreferences.getString(key, defaultValue);
    }

    @DebugLog
    public static void putLong(String key, long l) {
        getInstance().sharedPreferencesEditor.putLong(key, l).commit();
    }

    @DebugLog
    public static long getLong(String key, long defaultValue) {
        return getInstance().sharedPreferences.getLong(key, defaultValue);
    }

}
