package com.androidfu.foundation.events;

/**
 * Created by billmote on 9/7/14.
 */
public class GetQuoteOfTheDayEvent extends BaseEvent{
    public GetQuoteOfTheDayEvent(int callNumber) {
        super(callNumber);
    }
}