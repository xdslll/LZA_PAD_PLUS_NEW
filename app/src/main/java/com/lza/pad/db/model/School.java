package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/7/15.
 */
public class School implements Parcelable {

    String id;

    String bh;

    String title;

    String password;

    String max_authority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMax_authority() {
        return max_authority;
    }

    public void setMax_authority(String max_authority) {
        this.max_authority = max_authority;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.bh);
        dest.writeString(this.title);
        dest.writeString(this.password);
        dest.writeString(this.max_authority);
    }

    public School() {
    }

    private School(Parcel in) {
        this.id = in.readString();
        this.bh = in.readString();
        this.title = in.readString();
        this.password = in.readString();
        this.max_authority = in.readString();
    }

    public static final Parcelable.Creator<School> CREATOR = new Parcelable.Creator<School>() {
        public School createFromParcel(Parcel source) {
            return new School(source);
        }

        public School[] newArray(int size) {
            return new School[size];
        }
    };
}
