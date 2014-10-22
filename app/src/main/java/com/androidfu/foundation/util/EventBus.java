package com.androidfu.foundation.util;

import com.squareup.otto.Bus;

import hugo.weaving.DebugLog;

/**
 * Created by Bill on 8/1/14.
 */
public class EventBus {

    private static EventBus mInstance;
    private final Bus mBus;

    @DebugLog
    private EventBus() {
        // Don't let this class get instantiated directly.
        mBus = new Bus();
    }

    // @DebugLog intentionally omitted
    public static EventBus getInstance() {
        if (mInstance == null) {
            mInstance = new EventBus();
        }
        return mInstance;
    }

    @DebugLog
    public static void post(Object event) {
        getInstance().mBus.post(event);
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
        }    }

}
