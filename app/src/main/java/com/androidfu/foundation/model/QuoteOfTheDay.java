package com.androidfu.foundation.model;

import com.google.gson.annotations.Expose;

public class QuoteOfTheDay extends BaseModel {

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Expose
    private String quote;

}