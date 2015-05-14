package lza.com.lza.library.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import lza.com.lza.library.util.AppLogger;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 14-9-10.
 */
public abstract class BaseDao<T, ID> {

    protected Dao<T, ID> mDao;
    protected QueryBuilder<T, ID> mQuery = null;
    protected Where<T, ID> mWhere = null;
    protected UpdateBuilder<T, ID> mUpdate = null;
    protected DeleteBuilder<T, ID> mDelete = null;
    protected Context mContext;

    public BaseDao(Context context) {
        mContext = context;
    }

    public Dao<T, ID> getDao() {
        return mDao;
    }

    public boolean isExists(ID id) {
        try {
            return mDao.idExists(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int create(T data) {
        try {
            return mDao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void createIfNotExists(Collection<T> datas) {
        if (datas != null) {
            for (T data : datas) {
                try {
                    mDao.createIfNotExists(data);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean createIfNotExists(T data) {
        if (mDao != null) {
            try {
                T _data = mDao.createIfNotExists(data);
                if (_data != null) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public Dao.CreateOrUpdateStatus createOrUpdate(T data) {
        if (mDao != null) {
            try {
                return mDao.createOrUpdate(data);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public int updateData(T data) {
        if (mDao != null) {
            try {
                return mDao.update(data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public long count() {
        try {
            return mDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long countOfCondition() {
        try {
            if (mQuery == null) {
                createQueryBuilder();
            }
            mQuery.setCountOf(true);
            return countOfCondition(mQuery.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long countOfCondition(PreparedQuery<T> prepare) {
        if (prepare != null) {
            AppLogger.e(prepare.toString());
            try {
                return mDao.countOf(prepare);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public T queryForId(ID id) {
        try {
            return mDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> queryForAll() {
        try {
            return mDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> queryForCondition(QueryBuilder<T, ID> queryBuilder) {
        try {
            PreparedQuery<T> prepare = queryBuilder.prepare();
            return queryForCondition(prepare);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected List<T> queryForCondition(PreparedQuery<T> condition) {
        if (condition != null) {
            AppLogger.e(condition.toString());
            try {
                return mDao.query(condition);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected List<T> queryForCondition() {
        try {
            if (mQuery == null) {
                createQueryBuilder();
            }
            return queryForCondition(mQuery.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T queryForFirst() {
        try {
            if (mQuery == null) {
                createQueryBuilder();
            }
            PreparedQuery<T> preparedQuery = mQuery.prepare();
            AppLogger.e("sql --> " + preparedQuery.toString());
            return mDao.queryForFirst(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteByRaw(String table_name) {
        String sql = "DELETE FROM " + table_name;
        try {
            mDao.queryRawValue(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int delete(T data) {
        try {
            return mDao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long clear() {
        List<T> datas = queryForAll();
        if (datas != null && datas.size() > 0) {
            try {
                return mDao.delete(datas);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int updateByBuidler() {
        try {
            if (mUpdate == null) {
                createUpdateBuilder();
            }
            return mDao.update(mUpdate.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateByValue(String colName, Object value) {
        try {
            if (mUpdate == null) {
                createUpdateBuilder();
            }
            mUpdate.updateColumnValue(colName, value);
            return updateByBuidler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updateById(String colName, String colValue, String idColName, ID id) {
        try {
            if (mUpdate == null) {
                createUpdateBuilder();
            }
            mUpdate.updateColumnValue(colName, colValue).where().eq(idColName, id);
            return updateByBuidler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int deleteByColumnValue(String colName, Object value) {
        try {
            if (mDelete == null) {
                createDeleteBuilder();
            }
            Where<T, ID> mDeleteWhere = mDelete.where();
            mDeleteWhere.eq(colName, value);
            mDelete.setWhere(mDeleteWhere);
            return mDao.delete(mDelete.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    protected void createQueryBuilder() {
        mQuery = mDao.queryBuilder();
    }

    protected void createQueryAndWhere() {
        createQueryBuilder();
        if (mQuery != null)
            mWhere = mQuery.where();
    }

    protected void createUpdateBuilder() {
        mUpdate = mDao.updateBuilder();
    }

    protected void createDeleteBuilder() {
        mDelete = mDao.deleteBuilder();
    }

    protected abstract boolean checkIfDuplicated(T data);
}
