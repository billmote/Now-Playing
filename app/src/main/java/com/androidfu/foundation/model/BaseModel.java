package com.androidfu.foundation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

/**
 * This class ease the use of ORMLite in the
 * providing a the remoteId (which is the
 * identification on the server) and the localId
 * (which is the identification in our local
 * database).
 */
public abstract class BaseModel {
    @DatabaseField(generatedId = true)
    private int localid;

    @DatabaseField
    @Expose
    @SerializedName("_id")
    private String remoteid;

    @DatabaseField
    private boolean dirty = false;

    @DatabaseField
    private boolean deleted = false;

    public int getLocalid() {
        return localid;
    }

    public void setLocalid(int localid) {
        this.localid = localid;
    }

    public String getRemoteid() {
        return remoteid;
    }

    public void setRemoteid(String remoteid) {
        this.remoteid = remoteid;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}