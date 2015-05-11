package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/11/15.
 */
@DatabaseTable
public class VersionModule implements Parcelable {

    @DatabaseField(id = true)
    String id;

    @DatabaseField(canBeNull = false, foreign = true)
    Version version_id;

    @DatabaseField(canBeNull = false, foreign = true)
    Module module_id;

    @DatabaseField(canBeNull = true, foreign = true)
    ConfigGroup config_group_id;

    String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Version getVersion_id() {
        return version_id;
    }

    public void setVersion_id(Version version_id) {
        this.version_id = version_id;
    }

    public Module getModule_id() {
        return module_id;
    }

    public void setModule_id(Module module_id) {
        this.module_id = module_id;
    }

    public ConfigGroup getConfig_group_id() {
        return config_group_id;
    }

    public void setConfig_group_id(ConfigGroup config_group_id) {
        this.config_group_id = config_group_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.version_id, 0);
        dest.writeParcelable(this.module_id, 0);
        dest.writeParcelable(this.config_group_id, 0);
        dest.writeString(this.label);
    }

    public VersionModule() {
    }

    private VersionModule(Parcel in) {
        this.id = in.readString();
        this.version_id = in.readParcelable(Version.class.getClassLoader());
        this.module_id = in.readParcelable(Module.class.getClassLoader());
        this.config_group_id = in.readParcelable(ConfigGroup.class.getClassLoader());
        this.label = in.readString();
    }

    public static final Parcelable.Creator<VersionModule> CREATOR = new Parcelable.Creator<VersionModule>() {
        public VersionModule createFromParcel(Parcel source) {
            return new VersionModule(source);
        }

        public VersionModule[] newArray(int size) {
            return new VersionModule[size];
        }
    };
}
