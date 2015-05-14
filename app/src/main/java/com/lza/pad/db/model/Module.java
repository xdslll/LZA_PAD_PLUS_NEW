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
@DatabaseTable(tableName = "module")
public class Module implements Parcelable {

    @DatabaseField(id = true)
    String id;

    @DatabaseField
    String name;

    @DatabaseField
    String ico;

    @DatabaseField
    String ico2;

    @DatabaseField
    String type;

    @DatabaseField
    String parse_path;

    @DatabaseField
    String url;

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

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParse_path() {
        return parse_path;
    }

    public void setParse_path(String parse_path) {
        this.parse_path = parse_path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIco2() {
        return ico2;
    }

    public void setIco2(String ico2) {
        this.ico2 = ico2;
    }

    public Module() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ico);
        dest.writeString(this.ico2);
        dest.writeString(this.type);
        dest.writeString(this.parse_path);
        dest.writeString(this.url);
    }

    private Module(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.ico = in.readString();
        this.ico2 = in.readString();
        this.type = in.readString();
        this.parse_path = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        public Module createFromParcel(Parcel source) {
            return new Module(source);
        }

        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    @Override
    public String toString() {
        return "Module{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ico='" + ico + '\'' +
                ", ico2='" + ico2 + '\'' +
                ", type='" + type + '\'' +
                ", parse_path='" + parse_path + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
