package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/21/15.
 */
public class PadJournalContent implements Parcelable {

    String id;

    String title;

    String school_bh;

    String source_type;

    String author;

    String pubdate;

    String contents;

    String clc;

    String url;

    String ico;

    String imgs;

    String isbn;

    String press;

    String fulltext;

    String bh;

    String title_c;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchool_bh() {
        return school_bh;
    }

    public void setSchool_bh(String school_bh) {
        this.school_bh = school_bh;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
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

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getClc() {
        return clc;
    }

    public void setClc(String clc) {
        this.clc = clc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getTitle_c() {
        return title_c;
    }

    public void setTitle_c(String title_c) {
        this.title_c = title_c;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.school_bh);
        dest.writeString(this.source_type);
        dest.writeString(this.author);
        dest.writeString(this.pubdate);
        dest.writeString(this.contents);
        dest.writeString(this.clc);
        dest.writeString(this.url);
        dest.writeString(this.ico);
        dest.writeString(this.imgs);
        dest.writeString(this.isbn);
        dest.writeString(this.press);
        dest.writeString(this.fulltext);
        dest.writeString(this.bh);
        dest.writeString(this.title_c);
    }

    public PadJournalContent() {
    }

    private PadJournalContent(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.school_bh = in.readString();
        this.source_type = in.readString();
        this.author = in.readString();
        this.pubdate = in.readString();
        this.contents = in.readString();
        this.clc = in.readString();
        this.url = in.readString();
        this.ico = in.readString();
        this.imgs = in.readString();
        this.isbn = in.readString();
        this.press = in.readString();
        this.fulltext = in.readString();
        this.bh = in.readString();
        this.title_c = in.readString();
    }

    public static final Creator<PadJournalContent> CREATOR = new Creator<PadJournalContent>() {
        public PadJournalContent createFromParcel(Parcel source) {
            return new PadJournalContent(source);
        }

        public PadJournalContent[] newArray(int size) {
            return new PadJournalContent[size];
        }
    };
}
