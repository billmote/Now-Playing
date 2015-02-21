package com.androidfu.nowplaying.model.application;

import com.androidfu.nowplaying.NowPlayingApplication;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

@DatabaseTable(tableName = "applicationsettings")
@DebugLog
public class ApplicationSettings {

    @DatabaseField(generatedId = true)
    private int _id;

    @Expose
    @SerializedName("is_app_disabled")
    @DatabaseField
    private boolean appDisabled;

    @Expose
    @SerializedName("is_update_nag_enabled")
    @DatabaseField
    private boolean updateNagEnabled;

    @Expose
    @SerializedName("message_of_the_day_title")
    @DatabaseField
    private String motdTitle;

    @Expose
    @SerializedName("motd_frequency")
    @DatabaseField
    private long motdFrequency;

    @Expose
    @SerializedName("contact_email")
    @DatabaseField
    private String contactEmail;

    @Expose
    @SerializedName("production_version_number")
    @DatabaseField
    private int prodVersionNum;

    @Expose
    @SerializedName("low_watermark_version_number")
    @DatabaseField
    private int lwmVersionNum;

    @Expose
    @SerializedName("kill_switch_message_text")
    @DatabaseField
    private String killSwitchMessageText;

    @Expose
    @SerializedName("mandatory_update_message_text")
    @DatabaseField
    private String mandatoryUpdateMessageText;

    @Expose
    @SerializedName("message_of_the_day_text")
    @DatabaseField
    private String motdMessageText;

    @Expose
    @SerializedName("terms_of_use_text")
    @DatabaseField
    private String termsOfUseText;

    @Expose
    @SerializedName("privacy_policy_text")
    @DatabaseField
    private String privacyPolicyText;

    @Expose
    private List<Version> versions = new ArrayList<Version>();
    @ForeignCollectionField(eager = true)

    /**
     * We need a container for the JSON array prior to storing the data in the database as there's
     * no ORMList container for a Java List.
     */
    private ForeignCollection<Version> versionsCollections;

    public boolean isAppDisabled() {
        return appDisabled;
    }

    public void setAppDisabled(boolean isAppDisabled) {
        this.appDisabled = isAppDisabled;
    }

    public boolean isUpdateNagEnabled() {
        return updateNagEnabled;
    }

    public void setUpdateNagEnabled(boolean isNagEnabled) {
        this.updateNagEnabled = isNagEnabled;
    }

    public int getProdVersionNum() {
        return prodVersionNum;
    }

    public void setProdVersionNum(int prodVersionNum) {
        this.prodVersionNum = prodVersionNum;
    }

    public int getLwmVersionNum() {
        return lwmVersionNum;
    }

    public void setLwmVersionNum(int lwmVersionNum) {
        this.lwmVersionNum = lwmVersionNum;
    }

    public String getKillSwitchMessageText() {
        return killSwitchMessageText;
    }

    public void setKillSwitchMessageText(String killSwitchMessageText) {
        this.killSwitchMessageText = killSwitchMessageText;
    }

    public String getMandatoryUpdateMessageText() {
        return mandatoryUpdateMessageText;
    }

    public void setMandatoryUpdateMessageText(String mandatoryUpdateMessageText) {
        this.mandatoryUpdateMessageText = mandatoryUpdateMessageText;
    }

    public String getMotdMessageText() {
        return motdMessageText;
    }

    public void setMotdMessageText(String motdMessageText) {
        this.motdMessageText = motdMessageText;
    }

    public String getTermsOfUseText() {
        return termsOfUseText;
    }

    public void setTermsOfUseText(String termsOfUseText) {
        this.termsOfUseText = termsOfUseText;
    }

    public String getPrivacyPolicyText() {
        return privacyPolicyText;
    }

    public void setPrivacyPolicyText(String privacyPolicyText) {
        this.privacyPolicyText = privacyPolicyText;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public String getMotdTitle() {
        return motdTitle;
    }

    public void setMotdTitle(String motdTitle) {
        this.motdTitle = motdTitle;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public long getMotdFrequency() {
        return motdFrequency;
    }

    public void setMotdFrequency(long motdFrequency) {
        this.motdFrequency = motdFrequency;
    }

    public ForeignCollection<Version> getVersionsCollections() {
        return versionsCollections;
    }

    public void setVersionsCollections(ForeignCollection<Version> versionsCollections) {
        this.versionsCollections = versionsCollections;
    }

    public String getChangeLog() {
        StringBuilder changes = new StringBuilder();
        String delimiter = "";
        for (Version version : this.getVersionsCollections()) {
            if (version.getVersionCode() > NowPlayingApplication.APP_VERSION_CODE) {
                changes.append(delimiter).append(version.getChanges());
                delimiter = "<br/>";
            }
        }
        return changes.toString();
    }
}
