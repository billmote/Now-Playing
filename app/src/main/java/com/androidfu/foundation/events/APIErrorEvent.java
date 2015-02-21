package com.androidfu.foundation.events;

import hugo.weaving.DebugLog;
import retrofit.RetrofitError;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class APIErrorEvent extends BaseEvent{
    private RetrofitError error;

    public APIErrorEvent(RetrofitError error, int callNumber){
        super(callNumber);
        this.error = error;
    }

    public RetrofitError getError() {
        return error;
    }

    public int getHttpStatusCode(){
        if (!this.error.isNetworkError() && this.error.getResponse()!=null) {
            return this.error.getResponse().getStatus();
        }else{
            return -1;
        }
    }

    public boolean isNetworkError(){
        return this.error.isNetworkError();
    }
}