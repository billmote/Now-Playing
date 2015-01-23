package com.androidfu.foundation.util;

import android.os.Build;

/**
 * Log -- An overridden version of android.util.Log that handles turning logging on/off as well as
 * setting the logging level.  The values for these fields can be found in res/values/build_properties.xml
 *
 * <pre>
 *         20140623 -- Added wtf logging and a wrapper to protect against API versions prior to FROYO (8)
 *         20130114 -- Code Review
 * </pre>
 *
 * @author Bill Mote
 */
public class Log {

    /**
     * Level of information to log, ASSERT (7) and ERROR (6) being the highest
     * numbers and least info and VERBOSE (2) and DEBUG (3) being the lowest
     * numbers and most info. Defaults to Verbose, the most information.
     */
    private static int logLevel = android.util.Log.VERBOSE;

    /**
     * Gives ability to turn logging on or off at a global level. Defaults to be
     * off. Can be set by the application.
     */
    private static boolean isLogging = false;

    private Log() {
        // Do not allow this class to be instantiated.
    }

    /**
     * @return if logging is on.
     */
    public static boolean isLogging() {
        return isLogging;
    }

    /**
     * Allows the application to set logging on or off.
     *
     * @param isLogging true if logging is on, false if logging is off
     */
    public static void setLogging(boolean isLogging) {
        Log.isLogging = isLogging;
    }

    /**
     * @return the level of information to log, ASSERT (7) and ERROR (6) being
     * the highest numbers and least info and VERBOSE (2) and DEBUG (3)
     * being the lowest numbers and most info.
     */
    public static int getLogLevel() {
        return logLevel;
    }

    /**
     * @param logLevel the level of information to log, ASSERT (7) and ERROR (6)
     *                 being the highest numbers and least info and VERBOSE (2) and
     *                 DEBUG (3) being the lowest numbers and most info.
     */
    public static void setLogLevel(int logLevel) {
        Log.logLevel = logLevel;
    }

    // 2
    public static int v(String tag, String msg) {
        if (logLevel <= android.util.Log.VERBOSE && isLogging) {
            return android.util.Log.v(tag, msg);
        }
        return 0;
    }

    // 2
    public static int v(String tag, String msg, Throwable tr) {
        if (logLevel <= android.util.Log.VERBOSE && isLogging) {
            return android.util.Log.v(tag, msg, tr);
        }
        return 0;
    }

    // 3
    public static int d(String tag, String msg) {
        if (logLevel <= android.util.Log.DEBUG && isLogging) {
            return android.util.Log.d(tag, msg);
        }
        return 0;
    }

    // 3
    public static int d(String tag, String msg, Throwable tr) {
        if (logLevel <= android.util.Log.DEBUG && isLogging) {
            return android.util.Log.d(tag, msg, tr);
        }
        return 0;
    }

    // 4
    public static int i(String tag, String msg) {
        if (logLevel <= android.util.Log.INFO && isLogging) {
            return android.util.Log.i(tag, msg);
        }
        return 0;
    }

    // 4
    public static int i(String tag, String msg, Throwable tr) {
        if (logLevel <= android.util.Log.INFO && isLogging) {
            return android.util.Log.i(tag, msg, tr);
        }
        return 0;
    }

    // 5
    public static int w(String tag, Throwable tr) {
        if (logLevel <= android.util.Log.WARN && isLogging) {
            return android.util.Log.w(tag, tr);
        }
        return 0;
    }

    // 5
    public static int w(String tag, String msg) {
        if (logLevel <= android.util.Log.WARN && isLogging) {
            return android.util.Log.w(tag, msg);
        }
        return 0;
    }

    // 5
    public static int w(String tag, String msg, Throwable tr) {
        if (logLevel <= android.util.Log.WARN && isLogging) {
            return android.util.Log.w(tag, msg, tr);
        }
        return 0;
    }

    // 6
    public static int e(String tag, String msg) {
        if (logLevel <= android.util.Log.ERROR && isLogging) {
            return android.util.Log.e(tag, msg);
        }
        return 0;
    }

    // 6
    public static int e(String tag, String msg, Throwable tr) {
        if (logLevel <= android.util.Log.ERROR && isLogging) {
            return android.util.Log.e(tag, msg, tr);
        }
        return 0;
    }

    // 7 or 6 if on API level 7 or less.
    public static int wtf(String tag, Throwable tr) {
        if (logLevel <= android.util.Log.ASSERT && isLogging) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                return android.util.Log.wtf(tag, tr);
            } else {
                // There is no e(String, Throwable) method
                return android.util.Log.e(tag, tr.getLocalizedMessage());
            }
        }
        return 0;
    }

    // 7 or 6 if on API level 7 or less.
    public static int wtf(String tag, String msg) {
        if (logLevel <= android.util.Log.ASSERT && isLogging) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                return android.util.Log.wtf(tag, msg);
            } else {
                return android.util.Log.e(tag, msg);
            }
        }
        return 0;
    }

    // 7 or 6 if on API level 7 or less.
    public static int wtf(String tag, String msg, Throwable tr) {
        if (logLevel <= android.util.Log.ASSERT && isLogging) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                return android.util.Log.wtf(tag, msg, tr);
            } else {
                return android.util.Log.e(tag, msg, tr);
            }
        }
        return 0;
    }

}