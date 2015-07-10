package com.lza.pad.db.dao;

import android.content.Context;

import com.lza.pad.db.model.EBookInfo;

/**
 *
 * Created by lansing on 2015/6/11.
 */
public class EBookDao extends LzaDao<EBookInfo, String> {

    private static EBookDao mInstance = null;

    public static EBookDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new EBookDao(context);
        }
        return mInstance;
    }

    private EBookDao(Context context) {
        super(context, EBookInfo.class);
    }

    @Override
    protected boolean checkIfDuplicated(EBookInfo data) {
        return false;
    }

}
