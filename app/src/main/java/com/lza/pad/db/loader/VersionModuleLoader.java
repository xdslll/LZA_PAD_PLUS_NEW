package com.lza.pad.db.loader;

import android.content.Context;

import com.lza.pad.db.dao.VersionModuleDao;
import com.lza.pad.db.model.VersionModule;

import java.util.List;

import lza.com.lza.library.db.AbstractLoader;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/13/15.
 */
public class VersionModuleLoader extends AbstractLoader<List<VersionModule>> {

    String mType;
    public VersionModuleLoader(Context context, String type) {
        super(context);
        mType = type;
    }

    @Override
    public List<VersionModule> loadInBackground() {
        return VersionModuleDao.getInstance(mContext).queryByType(mType);
    }
}
