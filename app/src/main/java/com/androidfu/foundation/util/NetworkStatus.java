package com.androidfu.foundation.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import com.androidfu.foundation.R;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/4/14.
 */
public class NetworkStatus {

    public interface NetworkStatusListener{
        public void onNetworkAvailable();
        public void onNetworkNotAvailable();

    }

    /**
     * Verifies the Internet connection availability.
     *
     * @param context {@link Context} object reference only.
     * @return returns <tt>true</tt> if network connection available, otherwise <tt>false</tt>
     * @throws IllegalArgumentException if the context is instance of {@link Activity}. So, you can use the <tt>checkInternetConnection(Activity activity, NetworkStatusListener listener)</tt> method ,if you want to verify the network availability in activity
     */
    @DebugLog
    public static boolean checkInternetConnection(Context context) {
        if (context instanceof Activity) {
            throw new IllegalArgumentException("Use checkInternetConnection(Activity activity, NetworkStatusListener listener) mthod instead of this");
        }

        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }


    }

    /**
     * Verifies the Internet connection availability and calls the callback methods in {@link NetworkStatusListener} class object based on the network status.
     * Also, displays an alert(popup) saying that there is no network connection.
     * <br><code>&lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;</code>
     *
     * @param activity {@link Activity} object reference only.
     * @param listener Reference object of {@link NetworkStatusListener} class. This will used to trigger the status of the network.
     */
    @DebugLog
    public static void checkInternetConnection(Activity activity, NetworkStatusListener listener) {
        checkInternetConnection(activity, listener, true);
    }

    /**
     * Verifies the Internet connection availability and calls the callback methods in {@link NetworkStatusListener} class object based on the network status.
     * Also, displays an alert(popup) saying that there is no network connection based on the <code>showAlertOnFailure</code> parameter value.
     * <br><code>&lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;</code>
     *
     * @param activity           {@link Activity} object reference only.
     * @param listener           Reference object of {@link NetworkStatusListener} class. This will used to trigger the status of the network.
     * @param showAlertOnFailure if <tt>true</tt> displays an alert, otherwise not.
     */
    @DebugLog
    public static void checkInternetConnection(Activity activity, NetworkStatusListener listener, boolean showAlertOnFailure) {
        ConnectivityManager conMgr = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            listener.onNetworkAvailable();
        } else {
            if (showAlertOnFailure)
                // Util.showAlert(activity, activity.getString(R.string.alert_title), activity.getString(R.string.no_internet));
            listener.onNetworkNotAvailable();
        }
    }

}
