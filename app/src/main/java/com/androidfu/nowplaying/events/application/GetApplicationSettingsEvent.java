package com.androidfu.nowplaying.events.application;

import com.androidfu.nowplaying.events.BaseEvent;

import hugo.weaving.DebugLog;

/**
 * Created by billmote on 9/7/14.
 */
@DebugLog
public class GetApplicationSettingsEvent extends BaseEvent {
    public GetApplicationSettingsEvent(int callNumber) {
        super(callNumber);
    }
}
