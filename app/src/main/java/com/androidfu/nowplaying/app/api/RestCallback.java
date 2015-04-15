package com.androidfu.nowplaying.app.api;

import com.androidfu.nowplaying.app.events.APIErrorEvent;
import com.androidfu.nowplaying.app.events.CaptivePortalAuthReqEvent;
import com.androidfu.nowplaying.app.util.EventBus;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public abstract class RestCallback<T> implements Callback<T> {
    private int callNumber;

    public RestCallback(int callNumber) {
        this.callNumber = callNumber;
    }

    @Override
    public void failure(RetrofitError error) {
        if (error.getMessage().contains("Trust anchor for certification path not found.")) {
            EventBus.post(new CaptivePortalAuthReqEvent());
        } else {
            EventBus.post(new APIErrorEvent(error, this.callNumber));
        }
    }

    public int getCallNumber() {
        return callNumber;
    }
}
