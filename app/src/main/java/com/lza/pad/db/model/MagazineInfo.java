package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 期刊
 *
 * Created by lansing on 2015/6/4.
 */
public class MagazineInfo implements Parcelable {
    // 标题
    private String title;
    // 发布时间
    private String intime;
    // 期刊id
    private String id;
    // 出版社
    private String press;
    // issn
    private String issn;
    // 主办单位
    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.intime);
        dest.writeString(this.id);
        dest.writeString(this.press);
        dest.writeString(this.issn);
        dest.writeString(this.company);
    }

    public MagazineInfo() {
    }

    private MagazineInfo(Parcel in) {
        this.title = in.readString();
        this.intime = in.readString();
        this.id = in.readString();
        this.press = in.readString();
        this.issn = in.readString();
        this.company = in.readString();
    }

    public static final Parcelable.Creator<MagazineInfo> CREATOR = new Parcelable.Creator<MagazineInfo>() {
        public MagazineInfo createFromParcel(Parcel source) {
            return new MagazineInfo(source);
        }

        public MagazineInfo[] newArray(int size) {
            return new MagazineInfo[size];
        }
    };
}
