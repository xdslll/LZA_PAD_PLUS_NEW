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
@DatabaseTable(tableName = "config_group")
public class ConfigGroup implements Parcelable {

    @DatabaseField(id = true)
    String id;

    @DatabaseField
    String name;

    @DatabaseField
    String description;

    @DatabaseField
    String value;

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

    public ConfigGroup() {
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
    }

    private ConfigGroup(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.value = in.readString();
    }

    public static final Creator<ConfigGroup> CREATOR = new Creator<ConfigGroup>() {
        public ConfigGroup createFromParcel(Parcel source) {
            return new ConfigGroup(source);
        }

        public ConfigGroup[] newArray(int size) {
            return new ConfigGroup[size];
        }
    };

    @Override
    public String toString() {
        return "ConfigGroup{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
