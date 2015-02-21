
package com.androidfu.nowplaying.model.movies;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

@Parcel
@DebugLog
public class AbridgedCast {

    @Expose
    private String name;
    @Expose
    private String id;
    @Expose
    private List<String> characters = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

}
