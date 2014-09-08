package com.androidfu.foundation.events;

import retrofit.RetrofitError;

/**
 * Created by billmote on 9/7/14.
 */
public class ApiErrorEvent extends BaseEvent{
    private RetrofitError error;

    public ApiErrorEvent(RetrofitError error, int callNumber){
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