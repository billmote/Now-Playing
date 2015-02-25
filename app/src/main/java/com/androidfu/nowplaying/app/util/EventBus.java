package com.androidfu.nowplaying.app.util;

import com.squareup.otto.Bus;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/1/14.
 */
@DebugLog
public class EventBus {

    private static EventBus mInstance;
    private final Bus mBus;

    private EventBus() {
        // Don't let this class get instantiated directly.
        mBus = new Bus();
    }

    public static EventBus getInstance() {
        if (mInstance == null) {
            mInstance = new EventBus();
        }
        return mInstance;
    }

    public static void post(Object event) {
        getInstance().mBus.post(event);
    }

    public static void register(Object subscriber) {
        getInstance().mBus.register(subscriber);
    }

    public static void unregister(Object subscriber) {
        try {
            getInstance().mBus.unregister(subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
