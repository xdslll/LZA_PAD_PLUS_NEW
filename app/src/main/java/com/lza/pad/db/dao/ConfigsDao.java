package com.lza.pad.db.dao;

/**
 *
 * Created by lansing on 2015/5/26.
 */

import android.content.Context;

import com.lza.pad.db.model.Configs;


public class ConfigsDao extends LzaDao<Configs, String> {

    private static ConfigsDao mInstance = null;

    public static ConfigsDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConfigsDao(context);
        }
        return mInstance;
    }

    private ConfigsDao(Context context) {
        super(context, Configs.class);
    }

    @Override
    protected boolean checkIfDuplicated(Configs data) {
        return false;
    }

}
