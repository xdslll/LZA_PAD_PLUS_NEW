package com.lza.pad.db.dao;

import android.content.Context;

import com.lza.pad.db.model.ModuleType;

/**
 * Created by lansing on 2015/5/22.
 */
public class ModuleTypeDao extends LzaDao<ModuleType , String>{

    private static ModuleTypeDao mInstance = null;

    public static ModuleTypeDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ModuleTypeDao(context);
        }
        return mInstance;
    }

    private ModuleTypeDao(Context context) {
        super(context, ModuleType.class);
    }

    @Override
    protected boolean checkIfDuplicated(ModuleType data) {
        return false;
    }
}
