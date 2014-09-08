package com.androidfu.foundation.model;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "applicationsettings")
public class ApplicationSettings extends BaseModel {

    @Expose
    @DatabaseField
    private boolean app_disabled;

    @Expose
    @DatabaseField
    private boolean upg_nag_enabled;

    @Expose
    @DatabaseField
    private int production_version_number;

    @Expose
    @DatabaseField
    private int low_watermark_version_number;

    @Expose
    @DatabaseField
    private String kill_switch_message_text;

    @Expose
    @DatabaseField
    private String mandatory_update_message_text;

    @Expose
    @DatabaseField
    private String message_of_the_day_text;

    @Expose
    @DatabaseField
    private String update_nag_message_text;

    @Expose
    @DatabaseField
    private Long last_updated_on;

    @Expose
    @DatabaseField
    private String terms_of_use_text;

    @Expose
    @DatabaseField
    private String privacy_policy_text;

    public boolean isApp_disabled() {
        return app_disabled;
    }

    public void setApp_disabled(boolean isAppDisabled) {
        this.app_disabled = isAppDisabled;
    }

    public boolean isUpg_nag_enabled() {
        return upg_nag_enabled;
    }

    public void setUpg_nag_enabled(boolean isNagEnabled) {
        this.upg_nag_enabled = isNagEnabled;
    }

    public int getProduction_version_number() {
        return production_version_number;
    }

    public void setProduction_version_number(int production_version_number) {
        this.production_version_number = production_version_number;
    }

    public int getLow_watermark_version_number() {
        return low_watermark_version_number;
    }

    public void setLow_watermark_version_number(int low_watermark_version_number) {
        this.low_watermark_version_number = low_watermark_version_number;
    }

    public String getKill_switch_message_text() {
        return kill_switch_message_text;
    }

    public void setKill_switch_message_text(String kill_switch_message_text) {
        this.kill_switch_message_text = kill_switch_message_text;
    }

    public String getMandatory_update_message_text() {
        return mandatory_update_message_text;
    }

    public void setMandatory_update_message_text(String mandatory_update_message_text) {
        this.mandatory_update_message_text = mandatory_update_message_text;
    }

    public String getMessage_of_the_day_text() {
        return message_of_the_day_text;
    }

    public void setMessage_of_the_day_text(String message_of_the_day_text) {
        this.message_of_the_day_text = message_of_the_day_text;
    }

    public String getUpdate_nag_message_text() {
        return update_nag_message_text;
    }

    public void setUpdate_nag_message_text(String update_nag_message_text) {
        this.update_nag_message_text = update_nag_message_text;
    }

    public Long getLast_updated_on() {
        return last_updated_on;
    }

    public void setLast_updated_on(Long last_updated_on) {
        this.last_updated_on = last_updated_on;
    }

    public String getTerms_of_use_text() {
        return terms_of_use_text;
    }

    public void setTerms_of_use_text(String terms_of_use_text) {
        this.terms_of_use_text = terms_of_use_text;
    }

    public String getPrivacy_policy_text() {
        return privacy_policy_text;
    }

    public void setPrivacy_policy_text(String privacy_policy_text) {
        this.privacy_policy_text = privacy_policy_text;
    }
}