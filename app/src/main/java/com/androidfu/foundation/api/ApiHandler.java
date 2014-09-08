package com.androidfu.foundation.api;

import com.androidfu.foundation.events.ApiErrorEvent;
import com.androidfu.foundation.util.EventBus;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by billmote on 9/7/14.
 */
public abstract class ApiHandler<T> implements Callback<T> {
    private int callNumber;

    @DebugLog
    public ApiHandler(int callNumber) {
        this.callNumber = callNumber;
    }

    @DebugLog
    @Override
    public void failure(RetrofitError error) {
        EventBus.post(new ApiErrorEvent(error, this.callNumber));
    }

    @DebugLog
    public int getCallNumber() {
        return callNumber;
    }
}