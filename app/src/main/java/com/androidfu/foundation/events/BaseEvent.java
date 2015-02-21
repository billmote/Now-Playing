package com.androidfu.foundation.events;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class BaseEvent {
    public static final int ERROR_CLIENT = 400;
    public static final int ERROR_NEEDS_AUTHENTICATION = 401;
    public static final int ERROR_FORBBIDEN = 403;
    public static final int ERROR_NOT_FOUND = 404;
    public static final int ERROR_METHOD_UNALLOWED = 405;

    private int callNumber;

    public BaseEvent(int callNumber){
        this.callNumber = callNumber;
    }

    public int getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(int callNumber) {
        this.callNumber = callNumber;
    }}
