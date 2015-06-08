package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文献
 *
 * Created by lansing on 2015/6/4.
 */
public class LiteratureInfo implements Parcelable {
    // 标题
    private String title;
    // 作者
    private String author;
    // 内容
    private String content;
    // ...路径
    private String url;
    // 发表时间
    private String intime;
    // ....
    private String filename;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
        dest.writeString(this.intime);
        dest.writeString(this.filename);
    }

    public LiteratureInfo() {
    }

    private LiteratureInfo(Parcel in) {
        this.title = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
        this.intime = in.readString();
        this.filename = in.readString();
    }

    public static final Parcelable.Creator<LiteratureInfo> CREATOR = new Parcelable.Creator<LiteratureInfo>() {
        public LiteratureInfo createFromParcel(Parcel source) {
            return new LiteratureInfo(source);
        }

        public LiteratureInfo[] newArray(int size) {
            return new LiteratureInfo[size];
        }
    };
}
