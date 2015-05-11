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
public class Config implements Parcelable {

    @DatabaseField(id = true)
    String id;

    @DatabaseField
    String key;

    @DatabaseField
    String value;

    @DatabaseField
    String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.value);
        dest.writeString(this.description);
    }

    public Config() {
    }

    private Config(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.value = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
        public Config createFromParcel(Parcel source) {
            return new Config(source);
        }

        public Config[] newArray(int size) {
            return new Config[size];
        }
    };
}
