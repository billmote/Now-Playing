package com.androidfu.nowplaying.app.events.movies;

import com.androidfu.nowplaying.app.events.BaseEvent;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class GetMoviesEvent extends BaseEvent {
    private int pageLimit;
    private int pageNumber;

    public GetMoviesEvent(int pageNumber, int pageLimit) {
        super();
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
