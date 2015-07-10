package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by lansing on 2015/6/12.
 */
public class PadJournalYear implements Parcelable {

    private String year = null;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.year);
    }

    public PadJournalYear() {
    }

    private PadJournalYear(Parcel in) {
        this.year = in.readString();
    }

    public static final Creator<PadJournalYear> CREATOR = new Creator<PadJournalYear>() {
        public PadJournalYear createFromParcel(Parcel source) {
            return new PadJournalYear(source);
        }

        public PadJournalYear[] newArray(int size) {
            return new PadJournalYear[size];
        }
    };
}
