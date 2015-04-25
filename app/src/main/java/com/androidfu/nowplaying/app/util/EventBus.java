package com.androidfu.nowplaying.app.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/1/14.
 */
@DebugLog
public class EventBus {

    private static final Handler mainThread = new Handler(Looper.getMainLooper());
    private static EventBus mInstance;
    private final Bus mBus;

    @DebugLog
    private EventBus() {
        // Don't let this class get instantiated directly.
        mBus = new Bus();
    }

    // @DebugLog intentionally omitted
    private static EventBus getInstance() {
        if (mInstance == null) {
            mInstance = new EventBus();
        }
        return mInstance;
    }

    @DebugLog
    public static void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            getInstance().mBus.post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    post(event);
                }
            });
        }

    }

    @DebugLog
    public static void register(Object subscriber) {
        getInstance().mBus.register(subscriber);
    }

    @DebugLog
    public static void unregister(Object subscriber) {
        try {
            getInstance().mBus.unregister(subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
