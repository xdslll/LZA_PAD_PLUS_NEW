package com.lza.pad.ui.fragment.base;

import android.os.Bundle;

import com.lza.pad.db.model.User;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/12/15.
 */
public class BaseUserFragment extends BaseFragment {

    private User mUser = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(KEY_USER);
        }
    }

    protected User getUser() {
        return mUser;
    }
}
