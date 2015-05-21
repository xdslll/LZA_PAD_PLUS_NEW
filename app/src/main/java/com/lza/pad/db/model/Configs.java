package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/15/15.
 */
public class Configs implements Parcelable {

    String id;

    List<Config> config_id;

    List<ConfigGroup> config_group_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Config> getConfig_id() {
        return config_id;
    }

    public void setConfig_id(List<Config> config_id) {
        this.config_id = config_id;
    }

    public List<ConfigGroup> getConfig_group_id() {
        return config_group_id;
    }

    public void setConfig_group_id(List<ConfigGroup> config_group_id) {
        this.config_group_id = config_group_id;
    }

    public Configs() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeTypedList(config_id);
        dest.writeTypedList(config_group_id);
    }

    private Configs(Parcel in) {
        this.id = in.readString();
        in.readTypedList(config_id, Config.CREATOR);
        in.readTypedList(config_group_id, ConfigGroup.CREATOR);
    }

    public static final Creator<Configs> CREATOR = new Creator<Configs>() {
        public Configs createFromParcel(Parcel source) {
            return new Configs(source);
        }

        public Configs[] newArray(int size) {
            return new Configs[size];
        }
    };
}
