package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * 期刊
 *
 * Created by lansing on 2015/6/4.
 */
public class MagazineInfo implements Parcelable {

    // 期刊简介信息
    private PadJournalContent contents_qk = null;

    // 期刊年限信息
    private List<PadJournalYear> contents_qk_year = null;

    // 期刊期数信息
    private List<PadJournalPeriod> contents = null;

    public PadJournalContent getContents_qk() {
        return contents_qk;
    }

    public void setContents_qk(PadJournalContent contents_qk) {
        this.contents_qk = contents_qk;
    }

    public List<PadJournalYear> getContents_qk_year() {
        return contents_qk_year;
    }

    public void setContents_qk_year(List<PadJournalYear> contents_qk_year) {
        this.contents_qk_year = contents_qk_year;
    }

    public List<PadJournalPeriod> getContents() {
        return contents;
    }

    public void setContents(List<PadJournalPeriod> contents) {
        this.contents = contents;
    }

    public MagazineInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.contents_qk, 0);
        dest.writeTypedList(contents_qk_year);
        dest.writeTypedList(contents);
    }

    private MagazineInfo(Parcel in) {
        this.contents_qk = in.readParcelable(PadJournalContent.class.getClassLoader());
        in.readTypedList(contents_qk_year, PadJournalYear.CREATOR);
        in.readTypedList(contents, PadJournalPeriod.CREATOR);
    }

    public static final Creator<MagazineInfo> CREATOR = new Creator<MagazineInfo>() {
        public MagazineInfo createFromParcel(Parcel source) {
            return new MagazineInfo(source);
        }

        public MagazineInfo[] newArray(int size) {
            return new MagazineInfo[size];
        }
    };
}
