package com.lza.pad.db.dao;

import android.content.Context;

import com.lza.pad.db.model.Config;

/**
 * Created by lansing on 2015/5/27.
 */
public class ConfigDao extends LzaDao<Config, String> {

    private static ConfigDao mInstance = null;

    public static ConfigDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConfigDao(context);
        }
        return mInstance;
    }

    private ConfigDao(Context context) {
        super(context, Config.class);
    }

    @Override
    protected boolean checkIfDuplicated(Config data) {
        return false;
    }

}
