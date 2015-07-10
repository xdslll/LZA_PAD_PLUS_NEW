package com.lza.pad.db.loader;

import android.content.Context;

import com.lza.pad.db.dao.ConfigDao;
import com.lza.pad.db.dao.EBookDao;
import com.lza.pad.db.model.Config;
import com.lza.pad.db.model.EBookInfo;

import java.util.List;

import lza.com.lza.library.db.AbstractLoader;

/**
 *
 * Created by lansing on 2015/6/11.
 */
public class EBookLoader extends AbstractLoader<List<EBookInfo>> {

    public EBookLoader(Context context) {
        super(context);
    }

    @Override
    public List<EBookInfo> loadInBackground() {
        return EBookDao.getInstance(mContext).queryForAll();
    }
}
