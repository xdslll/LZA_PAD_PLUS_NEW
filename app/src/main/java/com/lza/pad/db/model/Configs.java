package com.lza.pad.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/15/15.
 */
@DatabaseTable(tableName = "configs")
public class Configs implements Parcelable {

    @DatabaseField(id = true)
    String id;

    List<Config> config_id;

    List<ConfigGroup> config_group_id;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = "config_id")
    Config config;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = "config_group_id")
    ConfigGroup configGroup;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public ConfigGroup getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(ConfigGroup configGroup) {
        this.configGroup = configGroup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Config> getConfig_id() {
        return config_id;
    }

    public void setConfig_id(List<Config> config_id) {
        this.config_id = config_id;
    }

    public List<ConfigGroup> getConfig_group_id() {
        return config_group_id;
    }

    public void setConfig_group_id(List<ConfigGroup> config_group_id) {
        this.config_group_id = config_group_id;
    }

    public Configs() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeTypedList(config_id);
        dest.writeTypedList(config_group_id);
        dest.writeParcelable(this.config, 0);
        dest.writeParcelable(this.configGroup, 0);
    }

    private Configs(Parcel in) {
        this.id = in.readString();
        in.readTypedList(config_id, Config.CREATOR);
        in.readTypedList(config_group_id, ConfigGroup.CREATOR);
        this.config = in.readParcelable(Config.class.getClassLoader());
        this.configGroup = in.readParcelable(ConfigGroup.class.getClassLoader());
    }

    public static final Creator<Configs> CREATOR = new Creator<Configs>() {
        public Configs createFromParcel(Parcel source) {
            return new Configs(source);
        }

        public Configs[] newArray(int size) {
            return new Configs[size];
        }
    };
}
