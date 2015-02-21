package com.androidfu.foundation.events.movies;

import com.androidfu.foundation.events.BaseEvent;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class GetMoviesEvent extends BaseEvent {
    public GetMoviesEvent(int callNumber) {
        super(callNumber);
    }
}