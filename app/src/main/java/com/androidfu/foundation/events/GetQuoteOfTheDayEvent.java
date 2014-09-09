package com.androidfu.foundation.events;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
public class GetQuoteOfTheDayEvent extends BaseEvent{
    @DebugLog
    public GetQuoteOfTheDayEvent(int callNumber) {
        super(callNumber);
    }
}