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

    private static UserDao mInstance = null;

    public static UserDao getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserDao(context);
        }
        return mInstance;
    }

    private UserDao(Context context) {
        super(context, User.class);
    }

    @Override
    protected boolean checkIfDuplicated(User data) {
        return false;
    }

    public static User getLoginUser(Context context) {
        return getInstance(context).queryForFirst();
    }

}
