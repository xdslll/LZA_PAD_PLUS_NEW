package com.lza.pad.db.dao;

import android.content.Context;

import com.lza.pad.db.model.ConfigGroup;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/9/15.
 */
public class ConfigGroupDao extends LzaDao<ConfigGroup, String> {

    private static ConfigGroupDao mInstance = null;

    public static ConfigGroupDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConfigGroupDao(context);
        }
        return mInstance;
    }

    private ConfigGroupDao(Context context) {
        super(context, ConfigGroup.class);
    }

    @Override
    protected boolean checkIfDuplicated(ConfigGroup data) {
        return false;
    }

}
