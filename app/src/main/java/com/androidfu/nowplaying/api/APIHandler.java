package com.androidfu.nowplaying.api;

import com.androidfu.nowplaying.events.APIErrorEvent;
import com.androidfu.nowplaying.util.EventBus;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public abstract class APIHandler<T> implements Callback<T> {
    private int callNumber;

    public APIHandler(int callNumber) {
        this.callNumber = callNumber;
    }

    @Override
    public void failure(RetrofitError error) {
        EventBus.post(new APIErrorEvent(error, this.callNumber));
    }

    public int getCallNumber() {
        return callNumber;
    }
}
