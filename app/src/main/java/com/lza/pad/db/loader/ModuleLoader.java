package com.lza.pad.db.loader;

import android.content.Context;

import com.lza.pad.db.dao.ModuleDao;
import com.lza.pad.db.model.Module;

import java.util.List;

import lza.com.lza.library.db.AbstractLoader;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/13/15.
 */
public class ModuleLoader extends AbstractLoader<List<Module>> {

    public ModuleLoader(Context context) {
        super(context);
    }

    @Override
    public List<Module> loadInBackground() {
        return ModuleDao.getInstance(mContext).queryForAll();
    }
}
