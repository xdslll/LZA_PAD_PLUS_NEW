package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/7/15.
 */
public class SchoolVersion implements Parcelable {

    List<School> school_bh = new ArrayList<School>();

    List<Version> version_code = new ArrayList<Version>();

    String url;

    String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<School> getSchool_bh() {
        return school_bh;
    }

    public void setSchool_bh(List<School> school_bh) {
        this.school_bh = school_bh;
    }

    public List<Version> getVersion_code() {
        return version_code;
    }

    public void setVersion_code(List<Version> version_code) {
        this.version_code = version_code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SchoolVersion() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(school_bh);
        dest.writeTypedList(version_code);
        dest.writeString(this.url);
        dest.writeString(this.color);
    }

    private SchoolVersion(Parcel in) {
        in.readTypedList(school_bh, School.CREATOR);
        in.readTypedList(version_code, Version.CREATOR);
        this.url = in.readString();
        this.color = in.readString();
    }

    public static final Creator<SchoolVersion> CREATOR = new Creator<SchoolVersion>() {
        public SchoolVersion createFromParcel(Parcel source) {
            return new SchoolVersion(source);
        }

        public SchoolVersion[] newArray(int size) {
            return new SchoolVersion[size];
        }
    };
}
