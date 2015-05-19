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

    public static final String MODULE_TYPE_LOGIN = "LOGIN";

    public static final String COLUMN_MODULE = "module_id";

    @DatabaseField(id = true)
    String id;

    List<Version> version_id = new ArrayList<Version>();

    List<Module> module_id = new ArrayList<Module>();

    List<ConfigGroup> config_group_id = new ArrayList<ConfigGroup>();

    List<ModuleType> type = new ArrayList<ModuleType>();

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "module_id")
    Module module;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = "config_group_id")
    ConfigGroup config_group;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = "module_type_id")
    ModuleType module_type;

    @DatabaseField
    String label;

    @DatabaseField
    String index;

    @DatabaseField
    String need_login;

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

    public List<ModuleType> getType() {
        return type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setType(List<ModuleType> type) {
        this.type = type;
    }

    public ModuleType getModule_type() {
        return module_type;
    }

    public void setModule_type(ModuleType module_type) {
        this.module_type = module_type;
    }

    public String getNeed_login() {
        return need_login;
    }

    public void setNeed_login(String need_login) {
        this.need_login = need_login;
    }

    public VersionModule() {
    }

    @Override
    public String toString() {
        return "VersionModule{" +
                "id='" + id + '\'' +
                ", version_id=" + version_id +
                ", module_id=" + module_id +
                ", config_group_id=" + config_group_id +
                ", type=" + type +
                ", module=" + module +
                ", config_group=" + config_group +
                ", module_type=" + module_type +
                ", label='" + label + '\'' +
                ", index='" + index + '\'' +
                ", need_login='" + need_login + '\'' +
                '}';
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
        dest.writeTypedList(type);
        dest.writeParcelable(this.module, 0);
        dest.writeParcelable(this.config_group, 0);
        dest.writeParcelable(this.module_type, 0);
        dest.writeString(this.label);
        dest.writeString(this.index);
        dest.writeString(this.need_login);
    }

    private VersionModule(Parcel in) {
        this.id = in.readString();
        in.readTypedList(version_id, Version.CREATOR);
        in.readTypedList(module_id, Module.CREATOR);
        in.readTypedList(config_group_id, ConfigGroup.CREATOR);
        in.readTypedList(type, ModuleType.CREATOR);
        this.module = in.readParcelable(Module.class.getClassLoader());
        this.config_group = in.readParcelable(ConfigGroup.class.getClassLoader());
        this.module_type = in.readParcelable(ModuleType.class.getClassLoader());
        this.label = in.readString();
        this.index = in.readString();
        this.need_login = in.readString();
    }

    public static final Creator<VersionModule> CREATOR = new Creator<VersionModule>() {
        public VersionModule createFromParcel(Parcel source) {
            return new VersionModule(source);
        }

        public VersionModule[] newArray(int size) {
            return new VersionModule[size];
        }
    };
}
