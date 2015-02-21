package com.androidfu.nowplaying.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hugo.weaving.DebugLog;

@DebugLog
public class PackageReplacedReceiver extends BroadcastReceiver {

    public PackageReplacedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
            Your application was upgraded.  What do you need to do here?
            Set a preference flag to redisplay your Terms of Service?
            Re-register with GCM for push notifications?
        */
    }
}
