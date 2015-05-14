package com.lza.pad.ui.fragment.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lza.pad.R;
import com.lza.pad.db.dao.VersionModuleDao;
import com.lza.pad.db.model.VersionModule;
import com.lza.pad.ui.fragment.base.BaseUserFragment;

import java.util.List;

import lza.com.lza.library.util.AppLogger;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/14/15.
 */
public class TestDatabaseFragment extends BaseUserFragment {

    TextView mTxtInfo;
    Button mBtnQueryVersionModule, mBtnQueryModule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_db, container, false);
        mTxtInfo = (TextView) view.findViewById(R.id.test_db_info);
        mBtnQueryVersionModule = (Button) view.findViewById(R.id.test_db_query_version_module);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mBtnQueryVersionModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<VersionModule> versionModules = VersionModuleDao
                        .getInstance(mActivity).queryByType(VersionModule.MODULE_TYPE_NORMAL);
                AppLogger.e(versionModules.toString());
            }
        });
    }
}
