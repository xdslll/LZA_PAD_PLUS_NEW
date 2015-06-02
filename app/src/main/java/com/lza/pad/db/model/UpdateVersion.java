package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lansing on 2015/6/2.
 */
public class UpdateVersion implements Parcelable {

    private String id = null;

    // 新版本id
    private String version_code = null;

    // 版本名称
    private String version_name = null;

    // 版本下载地址
    private String url = null;

    // 版本描述
    private String upgrade_info = null;

    // 版本类型
    private String type = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpgrade_info() {
        return upgrade_info;
    }

    public void setUpgrade_info(String upgrade_info) {
        this.upgrade_info = upgrade_info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.version_code);
        dest.writeString(this.version_name);
        dest.writeString(this.url);
        dest.writeString(this.upgrade_info);
        dest.writeString(this.type);
    }

    public UpdateVersion() {
    }

    private UpdateVersion(Parcel in) {
        this.id = in.readString();
        this.version_code = in.readString();
        this.version_name = in.readString();
        this.url = in.readString();
        this.upgrade_info = in.readString();
        this.type = in.readString();
    }

    public static final Creator<UpdateVersion> CREATOR = new Creator<UpdateVersion>() {
        public UpdateVersion createFromParcel(Parcel source) {
            return new UpdateVersion(source);
        }

        public UpdateVersion[] newArray(int size) {
            return new UpdateVersion[size];
        }
    };
}
