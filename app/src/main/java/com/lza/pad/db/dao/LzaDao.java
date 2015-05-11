package com.lza.pad.db.dao;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.lza.pad.db.helper.LzaDatabaseHelper;

import java.sql.SQLException;

import lza.com.lza.library.db.BaseDao;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/5/15.
 */
public abstract class LzaDao<T, ID> extends BaseDao<T, ID> {

    public LzaDao(Context context, Class<T> clazz) {
        super(context);
        LzaDatabaseHelper helper = OpenHelperManager.getHelper(context, LzaDatabaseHelper.class);
        try {
            mDao = helper.getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
