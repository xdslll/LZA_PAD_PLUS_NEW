package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/15/15.
 */
@DatabaseTable(tableName = "module_type")
public class ModuleType implements Parcelable {

    public static final String DISPLAY_MODE_GRID = "GRID";
    public static final String DISPLAY_MODE_LIST = "LIST";

    @DatabaseField(id = true)
    String id;

    @DatabaseField
    String key;

    @DatabaseField
    String name;

    @DatabaseField
    String index;

    @DatabaseField
    String display_mode;

    @DatabaseField
    String need_login;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDisplay_mode() {
        return display_mode;
    }

    public void setDisplay_mode(String display_mode) {
        this.display_mode = display_mode;
    }

    public String getNeed_login() {
        return need_login;
    }

    public void setNeed_login(String need_login) {
        this.need_login = need_login;
    }

    public ModuleType() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.index);
        dest.writeString(this.display_mode);
        dest.writeString(this.need_login);
    }

    private ModuleType(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.index = in.readString();
        this.display_mode = in.readString();
        this.need_login = in.readString();
    }

    public static final Creator<ModuleType> CREATOR = new Creator<ModuleType>() {
        public ModuleType createFromParcel(Parcel source) {
            return new ModuleType(source);
        }

        public ModuleType[] newArray(int size) {
            return new ModuleType[size];
        }
    };

    @Override
    public String toString() {
        return "ModuleType{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", index='" + index + '\'' +
                ", display_mode='" + display_mode + '\'' +
                ", need_login='" + need_login + '\'' +
                '}';
    }
}
