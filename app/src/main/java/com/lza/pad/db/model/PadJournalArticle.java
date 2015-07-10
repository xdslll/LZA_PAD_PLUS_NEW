package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 2/28/15.
 */
public class PadJournalArticle implements Parcelable {

    private String id;

    private String system_id;

    private String title;

    private String jigou;

    private String keywords;

    private String contents;

    private String bh;

    private String url;

    private String pubdate;

    private String qi;

    private String entitle;

    private String author;

    private String enauthor;

    @SerializedName(value = "abstract")
    private String abstractStr;

    private String enkeywords;

    private String articlefrom;

    private String year;

    private String fenleihao;

    private String kancode;

    private String test_url;

    private String vol;

    private String doi;

    private String kan_name;

    private String enkanname;

    private String sr;

    private String page;

    private String a_id;

    private String school_id;

    private String from_id;

    private String xk_id;

    private String full_text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
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

    public String getJigou() {
        return jigou;
    }

    public void setJigou(String jigou) {
        this.jigou = jigou;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getQi() {
        return qi;
    }

    public void setQi(String qi) {
        this.qi = qi;
    }

    public String getEntitle() {
        return entitle;
    }

    public void setEntitle(String entitle) {
        this.entitle = entitle;
    }

    public String getEnauthor() {
        return enauthor;
    }

    public void setEnauthor(String enauthor) {
        this.enauthor = enauthor;
    }

    public String getAbstractStr() {
        return abstractStr;
    }

    public void setAbstractStr(String abstractStr) {
        this.abstractStr = abstractStr;
    }

    public String getEnkeywords() {
        return enkeywords;
    }

    public void setEnkeywords(String enkeywords) {
        this.enkeywords = enkeywords;
    }

    public String getArticlefrom() {
        return articlefrom;
    }

    public void setArticlefrom(String articlefrom) {
        this.articlefrom = articlefrom;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFenleihao() {
        return fenleihao;
    }

    public void setFenleihao(String fenleihao) {
        this.fenleihao = fenleihao;
    }

    public String getKancode() {
        return kancode;
    }

    public void setKancode(String kancode) {
        this.kancode = kancode;
    }

    public String getTest_url() {
        return test_url;
    }

    public void setTest_url(String test_url) {
        this.test_url = test_url;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getKan_name() {
        return kan_name;
    }

    public void setKan_name(String kan_name) {
        this.kan_name = kan_name;
    }

    public String getEnkanname() {
        return enkanname;
    }

    public void setEnkanname(String enkanname) {
        this.enkanname = enkanname;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getXk_id() {
        return xk_id;
    }

    public void setXk_id(String xk_id) {
        this.xk_id = xk_id;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public PadJournalArticle() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.system_id);
        dest.writeString(this.title);
        dest.writeString(this.jigou);
        dest.writeString(this.keywords);
        dest.writeString(this.contents);
        dest.writeString(this.bh);
        dest.writeString(this.url);
        dest.writeString(this.pubdate);
        dest.writeString(this.qi);
        dest.writeString(this.entitle);
        dest.writeString(this.author);
        dest.writeString(this.enauthor);
        dest.writeString(this.abstractStr);
        dest.writeString(this.enkeywords);
        dest.writeString(this.articlefrom);
        dest.writeString(this.year);
        dest.writeString(this.fenleihao);
        dest.writeString(this.kancode);
        dest.writeString(this.test_url);
        dest.writeString(this.vol);
        dest.writeString(this.doi);
        dest.writeString(this.kan_name);
        dest.writeString(this.enkanname);
        dest.writeString(this.sr);
        dest.writeString(this.page);
        dest.writeString(this.a_id);
        dest.writeString(this.school_id);
        dest.writeString(this.from_id);
        dest.writeString(this.xk_id);
        dest.writeString(this.full_text);
    }

    private PadJournalArticle(Parcel in) {
        this.id = in.readString();
        this.system_id = in.readString();
        this.title = in.readString();
        this.jigou = in.readString();
        this.keywords = in.readString();
        this.contents = in.readString();
        this.bh = in.readString();
        this.url = in.readString();
        this.pubdate = in.readString();
        this.qi = in.readString();
        this.entitle = in.readString();
        this.author = in.readString();
        this.enauthor = in.readString();
        this.abstractStr = in.readString();
        this.enkeywords = in.readString();
        this.articlefrom = in.readString();
        this.year = in.readString();
        this.fenleihao = in.readString();
        this.kancode = in.readString();
        this.test_url = in.readString();
        this.vol = in.readString();
        this.doi = in.readString();
        this.kan_name = in.readString();
        this.enkanname = in.readString();
        this.sr = in.readString();
        this.page = in.readString();
        this.a_id = in.readString();
        this.school_id = in.readString();
        this.from_id = in.readString();
        this.xk_id = in.readString();
        this.full_text = in.readString();
    }

    public static final Creator<PadJournalArticle> CREATOR = new Creator<PadJournalArticle>() {
        public PadJournalArticle createFromParcel(Parcel source) {
            return new PadJournalArticle(source);
        }

        public PadJournalArticle[] newArray(int size) {
            return new PadJournalArticle[size];
        }
    };
}
