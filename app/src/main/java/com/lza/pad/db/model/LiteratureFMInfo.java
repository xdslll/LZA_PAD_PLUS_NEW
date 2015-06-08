package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 期刊文献
 *
 * Created by lansing on 2015/6/4.
 */
public class LiteratureFMInfo implements Parcelable {
    // 文献 id
    private String id;
    // 期刊 id
    private String qkId;
    // 标题
    private String title;
    // 作者
    private String author;
    // 发布日期
    private String pubdate;
    // 内容
    private String content;
    // 路径
    private String url;
    // ....
    private String readyTag;
    // ....
    private String filename;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQkId() {
        return qkId;
    }

    public void setQkId(String qkId) {
        this.qkId = qkId;
    }

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

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
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

    public String getReadyTag() {
        return readyTag;
    }

    public void setReadyTag(String readyTag) {
        this.readyTag = readyTag;
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
        dest.writeString(this.id);
        dest.writeString(this.qkId);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.pubdate);
        dest.writeString(this.content);
        dest.writeString(this.url);
        dest.writeString(this.readyTag);
        dest.writeString(this.filename);
    }

    public LiteratureFMInfo() {
    }

    private LiteratureFMInfo(Parcel in) {
        this.id = in.readString();
        this.qkId = in.readString();
        this.title = in.readString();
        this.author = in.readString();
        this.pubdate = in.readString();
        this.content = in.readString();
        this.url = in.readString();
        this.readyTag = in.readString();
        this.filename = in.readString();
    }

    public static final Parcelable.Creator<LiteratureFMInfo> CREATOR = new Parcelable.Creator<LiteratureFMInfo>() {
        public LiteratureFMInfo createFromParcel(Parcel source) {
            return new LiteratureFMInfo(source);
        }

        public LiteratureFMInfo[] newArray(int size) {
            return new LiteratureFMInfo[size];
        }
    };
}
