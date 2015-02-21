package com.androidfu.foundation.events.movies;

import com.androidfu.foundation.events.BaseEvent;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class GetMoviesEvent extends BaseEvent {
    private int pageLimit;
    private int pageNumber;

    public GetMoviesEvent(int callNumber, int pageNumber, int pageLimit) {
        super(callNumber);
        this.pageNumber = pageNumber;
        this.pageLimit = pageLimit;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageLimit() {
        return pageLimit;
    }

    public void setPageLimit(int pageLimit) {
        this.pageLimit = pageLimit;
    }
}