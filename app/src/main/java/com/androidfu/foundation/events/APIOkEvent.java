package com.androidfu.foundation.events;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
public class APIOkEvent extends BaseEvent {
    @DebugLog
    public APIOkEvent(int callNumber) {
        super(callNumber);
    }
}
