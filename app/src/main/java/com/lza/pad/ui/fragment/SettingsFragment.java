package com.lza.pad.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lza.pad.ui.fragment.base.BaseUserFragment;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/12/15.
 */
public class SettingsFragment extends BaseUserFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView text = new TextView(mActivity);
        text.setText("设置");
        return text;
    }
}
