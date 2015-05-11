package lza.com.lza.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import lza.com.lza.library.util.AppLogger;

/**
 * Created by xiads on 14-9-1.
 */
public abstract class AbstractDatabaseHelper extends OrmLiteSqliteOpenHelper {

    protected Context mContext;
    protected int mResId;

    public AbstractDatabaseHelper(Context context, String db_name, int db_version) {
        super(context, db_name, null, db_version);
        mContext = context;
    }

    public AbstractDatabaseHelper(Context context, String db_name, int db_version, int config_res) {
        super(context, db_name, null, db_version, config_res);
        mContext = context;
        mResId = config_res;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        AppLogger.e("AbstractDatabaseHelper->onCreate");
        onCreateDatabase(database, connectionSource);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        AppLogger.e("AbstractDatabaseHelper->onUpgrade");
        if (newVersion > oldVersion) {
            onUpgradeDatabase(database, connectionSource, oldVersion, newVersion);
        }
    }

    public void createAllTables() {
        if (mContext != null && mResId > 0 && connectionSource != null) {
            DatabaseTools.createTables(mContext, mResId, connectionSource);
        }
    }

    public void dropAllTables() {
        if (mContext != null && mResId > 0 && connectionSource != null) {
            DatabaseTools.dropTables(mContext, mResId, connectionSource);
        }
    }

    public abstract void onCreateDatabase(SQLiteDatabase database, ConnectionSource connectionSource);

    public abstract void onUpgradeDatabase(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion);
}
