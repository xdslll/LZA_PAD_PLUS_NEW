package com.lza.pad.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.lza.pad.R;

import lza.com.lza.library.db.AbstractDatabaseHelper;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/5/15.
 */
public class LzaDatabaseHelper extends AbstractDatabaseHelper {

    public static final String DB_NAME = "lza.pad";

    public static final int DB_VERSION = 1;

    public LzaDatabaseHelper(Context context) {
        super(context, DB_NAME, DB_VERSION, R.raw.ormlite_config_v1);
    }

    @Override
    public void onCreateDatabase(SQLiteDatabase database, ConnectionSource connectionSource) {
        createAllTables();
    }

    @Override
    public void onUpgradeDatabase(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
