package com.androidfu.foundation.events;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
public class GetApplicationSettingsEvent extends BaseEvent{
    @DebugLog
    public GetApplicationSettingsEvent(int callNumber) {
        super(callNumber);
    }
}