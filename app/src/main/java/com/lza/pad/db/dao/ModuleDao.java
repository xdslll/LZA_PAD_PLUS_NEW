package com.lza.pad.db.dao;

import android.content.Context;

import com.lza.pad.db.model.Module;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/9/15.
 */
public class ModuleDao extends LzaDao<Module, Integer> {

    public ModuleDao(Context context) {
        super(context, Module.class);
    }

    @Override
    protected boolean checkIfDuplicated(Module data) {
        return false;
    }

}
