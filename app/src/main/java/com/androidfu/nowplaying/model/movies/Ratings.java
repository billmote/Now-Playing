
package com.androidfu.nowplaying.model.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import hugo.weaving.DebugLog;

@Parcel
@DebugLog
public class Ratings {

    @SerializedName("critics_rating")
    @Expose
    private String criticsRating;
    @SerializedName("critics_score")
    @Expose
    private Integer criticsScore;
    @SerializedName("audience_rating")
    @Expose
    private String audienceRating;
    @SerializedName("audience_score")
    @Expose
    private Integer audienceScore;

    public String getCriticsRating() {
        return criticsRating;
    }

    public void setCriticsRating(String criticsRating) {
        this.criticsRating = criticsRating;
    }

    public Integer getCriticsScore() {
        return criticsScore;
    }

    public void setCriticsScore(Integer criticsScore) {
        this.criticsScore = criticsScore;
    }

    public String getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(String audienceRating) {
        this.audienceRating = audienceRating;
    }

    public Integer getAudienceScore() {
        return audienceScore;
    }

    public void setAudienceScore(Integer audienceScore) {
        this.audienceScore = audienceScore;
    }

}
