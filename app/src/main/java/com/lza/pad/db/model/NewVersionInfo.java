package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lansing on 2015/6/2.
 */
public class NewVersionInfo implements Parcelable {

    private List<UpdateVersion> update_id = new ArrayList<UpdateVersion>();

    public List<UpdateVersion> getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(List<UpdateVersion> update_id) {
        this.update_id = update_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(update_id);
    }

    public NewVersionInfo() {
    }

    private NewVersionInfo(Parcel in) {
        in.readTypedList(update_id, UpdateVersion.CREATOR);
    }

    public static final Creator<NewVersionInfo> CREATOR = new Creator<NewVersionInfo>() {
        public NewVersionInfo createFromParcel(Parcel source) {
            return new NewVersionInfo(source);
        }

        public NewVersionInfo[] newArray(int size) {
            return new NewVersionInfo[size];
        }
    };
}
