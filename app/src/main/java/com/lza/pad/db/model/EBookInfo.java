package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 电子书
 *
 * Created by lansing on 2015/6/4.
 */
@DatabaseTable(tableName = "e_book")
public class EBookInfo implements Parcelable {
    // 系统编号
    @DatabaseField(id = true)
    private String id;
    // 图书编号
    private String bookId;

    // 书名(目录名称)
    @DatabaseField
    private String name;
    // ....
    private String namePy;
    // ISBN 国际标准图书编号
    private String isbn;

    // 作者
    @DatabaseField
    private String author;

    // 出版社
    @DatabaseField
    private String press;

    // 出版地
    private String address;

    // 出版日期
    @DatabaseField
    private String pubdate;

    // 页数
    private String pages;
    // 主题词
    private String subject;
    // 中图法(中国图书分类办法)
    private String ztf;

    // 摘要
    @DatabaseField
    private String summary;

    // 学科
    private String xk;

    // 封面url地址
    @DatabaseField
    private String img;

    // 阅读进度
    private int readPages;
    //目录中的page
    private int page;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePy() {
        return namePy;
    }

    public void setNamePy(String namePy) {
        this.namePy = namePy;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getZtf() {
        return ztf;
    }

    public void setZtf(String ztf) {
        this.ztf = ztf;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getXk() {
        return xk;
    }

    public void setXk(String xk) {
        this.xk = xk;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getReadPages() {
        return readPages;
    }

    public void setReadPages(int readPages) {
        this.readPages = readPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.bookId);
        dest.writeString(this.name);
        dest.writeString(this.namePy);
        dest.writeString(this.isbn);
        dest.writeString(this.author);
        dest.writeString(this.press);
        dest.writeString(this.address);
        dest.writeString(this.pubdate);
        dest.writeString(this.pages);
        dest.writeString(this.subject);
        dest.writeString(this.ztf);
        dest.writeString(this.summary);
        dest.writeString(this.xk);
        dest.writeString(this.img);
        dest.writeInt(this.readPages);
        dest.writeInt(this.page);
    }

    public EBookInfo() {
    }

    private EBookInfo(Parcel in) {
        this.id = in.readString();
        this.bookId = in.readString();
        this.name = in.readString();
        this.namePy = in.readString();
        this.isbn = in.readString();
        this.author = in.readString();
        this.press = in.readString();
        this.address = in.readString();
        this.pubdate = in.readString();
        this.pages = in.readString();
        this.subject = in.readString();
        this.ztf = in.readString();
        this.summary = in.readString();
        this.xk = in.readString();
        this.img = in.readString();
        this.readPages = in.readInt();
        this.page = in.readInt();
    }

    public static final Creator<EBookInfo> CREATOR = new Creator<EBookInfo>() {
        public EBookInfo createFromParcel(Parcel source) {
            return new EBookInfo(source);
        }

        public EBookInfo[] newArray(int size) {
            return new EBookInfo[size];
        }
    };
}
