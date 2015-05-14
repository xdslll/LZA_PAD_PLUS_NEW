package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/11/15.
 */
@DatabaseTable(tableName = "version_module")
public class VersionModule implements Parcelable {

    public static final String MODULE_TYPE_NORMAL = "0";

    public static final String MODULE_TYPE_LOGIN = "1";

    public static final String MODULE_TYPE_MYLIB = "2";

    public static final String COLUMN_TYPE = "type";

    @DatabaseField(id = true)
    String id;

    List<Version> version_id = new ArrayList<Version>();

    List<Module> module_id = new ArrayList<Module>();

    List<ConfigGroup> config_group_id = new ArrayList<ConfigGroup>();

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "mod_id")
    Module module;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = "cfg_group_id")
    ConfigGroup config_group;

    @DatabaseField
    String label;

    @DatabaseField
    String type;

    @DatabaseField
    String index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Version> getVersion_id() {
        return version_id;
    }

    public void setVersion_id(List<Version> version_id) {
        this.version_id = version_id;
    }

    public List<Module> getModule_id() {
        return module_id;
    }

    public void setModule_id(List<Module> module_id) {
        this.module_id = module_id;
    }

    public List<ConfigGroup> getConfig_group_id() {
        return config_group_id;
    }

    public void setConfig_group_id(List<ConfigGroup> config_group_id) {
        this.config_group_id = config_group_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public ConfigGroup getConfig_group() {
        return config_group;
    }

    public void setConfig_group(ConfigGroup config_group) {
        this.config_group = config_group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public VersionModule() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeTypedList(version_id);
        dest.writeTypedList(module_id);
        dest.writeTypedList(config_group_id);
        dest.writeParcelable(this.module, 0);
        dest.writeParcelable(this.config_group, 0);
        dest.writeString(this.label);
        dest.writeString(this.type);
        dest.writeString(this.index);
    }

    private VersionModule(Parcel in) {
        this.id = in.readString();
        in.readTypedList(version_id, Version.CREATOR);
        in.readTypedList(module_id, Module.CREATOR);
        in.readTypedList(config_group_id, ConfigGroup.CREATOR);
        this.module = in.readParcelable(Module.class.getClassLoader());
        this.config_group = in.readParcelable(ConfigGroup.class.getClassLoader());
        this.label = in.readString();
        this.type = in.readString();
        this.index = in.readString();
    }

    public static final Creator<VersionModule> CREATOR = new Creator<VersionModule>() {
        public VersionModule createFromParcel(Parcel source) {
            return new VersionModule(source);
        }

        public VersionModule[] newArray(int size) {
            return new VersionModule[size];
        }
    };

    @Override
    public String toString() {
        return "VersionModule{" +
                "id='" + id + '\'' +
                ", version_id=" + version_id +
                ", module_id=" + module_id +
                ", config_group_id=" + config_group_id +
                ", module=" + module +
                ", config_group=" + config_group +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}
