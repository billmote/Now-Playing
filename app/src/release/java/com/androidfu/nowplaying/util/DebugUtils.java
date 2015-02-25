package com.androidfu.nowplaying.app.util;

import android.app.Activity;

import hugo.weaving.DebugLog;

/**
 * Add this to your application's src/debug/... sources
 */
@DebugLog
public class DebugUtils {

    /**
     * Show the activity over the lockscreen and wake up the device. If you launched the app manually
     * both of these conditions are already true. If you deployed from the IDE, however, this will
     * save you from hundreds of power button presses and pattern swiping per day!
     */
    public static void riseAndShine(Activity activity) {
    }

}
