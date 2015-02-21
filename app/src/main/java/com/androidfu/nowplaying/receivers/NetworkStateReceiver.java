package com.androidfu.nowplaying.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.androidfu.nowplaying.events.network.NetworkAvailableEvent;
import com.androidfu.nowplaying.util.EventBus;

import hugo.weaving.DebugLog;

@DebugLog
public class NetworkStateReceiver extends BroadcastReceiver {

    public NetworkStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        // TODO Do something "real" with our receiver, but in the interim just post an event to the bus.
        EventBus.post(new NetworkAvailableEvent(wifi.isAvailable(), mobile.isAvailable()));

    }
}
