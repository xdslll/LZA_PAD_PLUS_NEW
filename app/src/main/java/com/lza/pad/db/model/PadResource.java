package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 15/2/14.
 */
public class PadResource implements Parcelable {

    /**
     * 资源类型：电子书
     */
    public static final String RESOURCE_EBOOK = "1";

    /**
     * 资源类型：电子教参
     */
    public static final String RESOURCE_EBOOK_JC = "2";

    /**
     * 资源类型：电子期刊
     */
    public static final String RESOURCE_JOURNAL = "3";

    /**
     * 资源类型：图片
     */
    public static final String RESOURCE_IMG = "4";

    /**
     * 资源类型：新闻
     */
    public static final String RESOURCE_NEWS = "5";

    /**
     * 资源类型：新书通知
     */
    public static final String RESOURCE_NEW_BOOK = "6";

    /**
     * 资源类型：热门借阅
     */
    public static final String RESOURCE_HOT_BOOK = "7";

    /**
     * 主键ID
     */
    @Expose
    private String id;

    /**
     * 标题
     */
    @Expose
    private String title;

    /**
     * 学校编号
     */
    @Expose
    private String school_bh;

    /**
     * 资源类型
     */
    @Expose
    private String source_type;

    /**
     * 作者
     */
    @Expose
    private String author;

    /**
     * 发布年份
     */
    @Expose
    private String pubdate;

    /**
     * 内容/摘要
     */
    @Expose
    private String contents;

    /**
     * 分类号
     */
    @Expose
    private String clc;

    /**
     * 全文地址
     */
    @Expose
    private String url;

    /**
     * 缩略图
     */
    @Expose
    private String ico;

    /**
     * 图片组
     */
    @Expose
    private String imgs;

    /**
     * ISBN/ISSN
     */
    @Expose
    private String isbn;

    /**
     * 出版社
     */
    @Expose
    private String press;

    /**
     * 全文下载地址
     */
    @Expose
    private String fulltext;

    /**
     * 资源特有的编号
     */
    @Expose
    private String bh;

    /**
     * 摘要
     */
    @Expose
    private String abs;

    /**
     * 资源的详细内容
     */
    private List<PadResourceDetail> mr = new ArrayList<PadResourceDetail>();

    /**
     * 期刊文章内容
     */
    private List<PadJournalArticle> article_list = new ArrayList<PadJournalArticle>();

    // 期刊简介信息
    private List<PadJournalContent> contents_qk = new ArrayList<PadJournalContent>();

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

    public List<PadResourceDetail> getMr() {
        return mr;
    }

    public void setMr(List<PadResourceDetail> mr) {
        this.mr = mr;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public List<PadJournalArticle> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<PadJournalArticle> article_list) {
        this.article_list = article_list;
    }

    public List<PadJournalContent> getContents_qk() {
        return contents_qk;
    }

    public void setContents_qk(List<PadJournalContent> contents_qk) {
        this.contents_qk = contents_qk;
    }

    public PadResource() {}

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
        dest.writeString(this.abs);
        dest.writeTypedList(mr);
        dest.writeTypedList(article_list);
        dest.writeTypedList(contents_qk);
    }

    private PadResource(Parcel in) {
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
        this.abs = in.readString();
        in.readTypedList(mr, PadResourceDetail.CREATOR);
        in.readTypedList(article_list, PadJournalArticle.CREATOR);
        in.readTypedList(contents_qk, PadJournalContent.CREATOR);
    }

    public static final Creator<PadResource> CREATOR = new Creator<PadResource>() {
        public PadResource createFromParcel(Parcel source) {
            return new PadResource(source);
        }

        public PadResource[] newArray(int size) {
            return new PadResource[size];
        }
    };
}
