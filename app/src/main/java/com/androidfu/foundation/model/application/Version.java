package com.androidfu.foundation.model.application;

import com.androidfu.foundation.model.application.ApplicationSettings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Bill on 9/11/14.
 */
@DatabaseTable(tableName = "version")
public class Version {

    @DatabaseField(generatedId = true)
    private int _id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private ApplicationSettings applicationSettings;

    @Expose
    @SerializedName("version_code")
    @DatabaseField
    private Integer versionCode;

    @Expose
    @DatabaseField
    private String changes;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int version_code) {
        this.versionCode = version_code;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }
}
