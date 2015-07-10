package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by lansing on 2015/6/12.
 */
public class PadJournalPeriod implements Parcelable{
    // id
    private String id = null;

    // 刊号
    private String kancode = null;

    // 年限
    private String year = null;

    // 期号
    private String qi = null;

    // 名称
    private String t_name = null;

    // 获取某期文献信息url
    private String full_text = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKancode() {
        return kancode;
    }

    public void setKancode(String kancode) {
        this.kancode = kancode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQi() {
        return qi;
    }

    public void setQi(String qi) {
        this.qi = qi;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.kancode);
        dest.writeString(this.year);
        dest.writeString(this.qi);
        dest.writeString(this.t_name);
        dest.writeString(this.full_text);
    }

    public PadJournalPeriod() {
    }

    private PadJournalPeriod(Parcel in) {
        this.id = in.readString();
        this.kancode = in.readString();
        this.year = in.readString();
        this.qi = in.readString();
        this.t_name = in.readString();
        this.full_text = in.readString();
    }

    public static final Creator<PadJournalPeriod> CREATOR = new Creator<PadJournalPeriod>() {
        public PadJournalPeriod createFromParcel(Parcel source) {
            return new PadJournalPeriod(source);
        }

        public PadJournalPeriod[] newArray(int size) {
            return new PadJournalPeriod[size];
        }
    };
}
