package com.lza.pad.db.dao;

import android.content.Context;

import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.VersionModule;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/9/15.
 */
public class VersionModuleDao extends LzaDao<VersionModule, String> {

    private static VersionModuleDao mInstance = null;

    public static VersionModuleDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VersionModuleDao(context);
        }
        return mInstance;
    }

    public VersionModuleDao(Context context) {
        super(context, VersionModule.class);
    }

    /*public List<VersionModule> queryByType(String type) {
        createQueryAndWhere();
        try {
            mWhere.eq(VersionModule.COLUMN_TYPE, type);
            return queryForCondition();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public int deleteByModule(Module module) {
        return deleteByColumnValue(VersionModule.COLUMN_MODULE, module);
    }

    @Override
    protected boolean checkIfDuplicated(VersionModule data) {
        return false;
    }

}
