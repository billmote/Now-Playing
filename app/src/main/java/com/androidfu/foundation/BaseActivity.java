package com.androidfu.foundation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.androidfu.foundation.model.SomePojo;
import com.androidfu.foundation.util.EventBus;
import com.androidfu.foundation.util.Log;
import com.squareup.otto.Subscribe;

import hugo.weaving.DebugLog;

public class BaseActivity extends Activity {

    public static final String TAG = BaseActivity.class.getSimpleName();

    NetworkListener networkListener;
    BroadcastReceiver receiver;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * IMPORTANT
         *
         * This does not work.  Parent classes will not get their subscription methods called.  See
         * the NetworkListener inner-class below which will work ;)  This is included for
         * illustration purposes only.
         */
        EventBus.register(this);

        networkListener = new NetworkListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final ConnectivityManager connMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                final android.net.NetworkInfo wifi = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                final android.net.NetworkInfo mobile = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if (wifi.isAvailable() || mobile.isAvailable()) {
                    networkListener.postNetworkState(intent.getAction());
                }
            }
        };
        registerReceiver(receiver, filter);    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @DebugLog
    @Subscribe
    /**
     * IMPORTANT
     *
     * This does not work.  Parent classes will not get their subscription methods called.  See
     * the NetworkListener inner-class below which will work ;)  This is included for
     * illustration purposes only.
     */
    public void getCurrentPojo(SomePojo pojo) {
        Log.e(TAG, String.format("This should never get called, but just in case ... our BaseActivity was listening and got: %1$s", pojo.someField));
    }

    /**
     * Otto only looks for 1st class objects.  If you want to subscribe to something in a parent class
     * you need to do so like this.
     */
    public class NetworkListener {

        public final String TAG = NetworkListener.class.getSimpleName();

        @DebugLog
        NetworkListener() {
            EventBus.register(this);
        }

        @DebugLog
        @Subscribe
        // Listen for our data response.  The name of this method is meaningless, but should describe what's going on.
        public void getCurrentPojo(SomePojo pojo) {
            Log.w(TAG, String.format("Our NetworkListener was listening and got: %1$s", pojo.someField));
        }

        @DebugLog
        public void postNetworkState(String string) {
            Log.d(TAG, String.format("Intent: %1$s", string));
            EventBus.post(String.format("Network is: %1$s", string));
        }

    }

}
