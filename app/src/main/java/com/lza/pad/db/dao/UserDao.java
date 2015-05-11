package com.lza.pad.db.dao;

import android.content.Context;

import com.lza.pad.db.model.User;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/9/15.
 */
public class UserDao extends LzaDao<User, Integer> {

    public UserDao(Context context) {
        super(context, User.class);
    }

    @Override
    protected boolean checkIfDuplicated(User data) {
        return false;
    }

}
