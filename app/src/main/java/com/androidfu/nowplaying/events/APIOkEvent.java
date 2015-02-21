package com.androidfu.nowplaying.events;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class APIOkEvent extends BaseEvent {
    public APIOkEvent(int callNumber) {
        super(callNumber);
    }
}
