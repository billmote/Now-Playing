package com.androidfu.nowplaying.model.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

@Parcel
@DebugLog
public class Movies {

    @Expose
    private int total;
    @Expose
    private List<Movie> movies = new ArrayList<Movie>();
    @Expose
    private Links_ links;
    @SerializedName("link_template")
    @Expose
    private String linkTemplate;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public Links_ getLinks() {
        return links;
    }

    public void setLinks(Links_ links) {
        this.links = links;
    }

    public String getLinkTemplate() {
        return linkTemplate;
    }

    public void setLinkTemplate(String linkTemplate) {
        this.linkTemplate = linkTemplate;
    }

}
