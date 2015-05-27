package com.lza.pad.db.loader;

import android.content.Context;

import com.lza.pad.db.dao.ConfigDao;
import com.lza.pad.db.model.Config;

import java.util.List;

import lza.com.lza.library.db.AbstractLoader;

/**
 * Created by lansing on 2015/5/27.
 */
public class ConfigLoader extends AbstractLoader<List<Config>> {

    public ConfigLoader(Context context) {
        super(context);
    }

    @Override
    public List<Config> loadInBackground() {
        return ConfigDao.getInstance(mContext).queryForAll();
    }
}
