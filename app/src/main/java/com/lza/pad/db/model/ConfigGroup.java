package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/11/15.
 */
@DatabaseTable
public class ConfigGroup implements Parcelable {

    @DatabaseField(id = true)
    String id;

    @DatabaseField
    String name;

    @DatabaseField
    String description;

    @DatabaseField
    String value;

    @DatabaseField(canBeNull = false, foreign = true)
    Config config_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Config getConfig_id() {
        return config_id;
    }

    public void setConfig_id(Config config_id) {
        this.config_id = config_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.value);
        dest.writeParcelable(this.config_id, 0);
    }

    public ConfigGroup() {
    }

    private ConfigGroup(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.value = in.readString();
        this.config_id = in.readParcelable(Config.class.getClassLoader());
    }

    public static final Parcelable.Creator<ConfigGroup> CREATOR = new Parcelable.Creator<ConfigGroup>() {
        public ConfigGroup createFromParcel(Parcel source) {
            return new ConfigGroup(source);
        }

        public ConfigGroup[] newArray(int size) {
            return new ConfigGroup[size];
        }
    };
}
