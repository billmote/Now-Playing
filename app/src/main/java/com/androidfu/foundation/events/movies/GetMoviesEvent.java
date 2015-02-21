package com.androidfu.foundation.events.movies;

import com.androidfu.foundation.events.BaseEvent;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class GetMoviesEvent extends BaseEvent {
    private int pageNumber;

    public GetMoviesEvent(int callNumber, int pageNumber) {
        super(callNumber);
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}