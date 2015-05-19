package com.lza.pad.ui.fragment.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.lza.pad.R;
import com.lza.pad.db.dao.ModuleDao;
import com.lza.pad.db.dao.VersionModuleDao;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.VersionModule;
import com.lza.pad.handler.SimpleHttpResponseHandler;
import com.lza.pad.ui.fragment.base.BaseUserFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lza.com.lza.library.util.AppLogger;
import lza.com.lza.library.util.GsonHelper;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/14/15.
 */
public class TestDatabaseFragment extends BaseUserFragment {

    TextView mTxtInfo;
    Button mBtnQueryVersionModule, mBtnSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_db, container, false);
        mTxtInfo = (TextView) view.findViewById(R.id.test_db_info);
        mBtnQueryVersionModule = (Button) view.findViewById(R.id.test_db_query_version_module);
        mBtnSend = (Button) view.findViewById(R.id.test_db_send);
        return view;
    }

    public static final String URL_PREX = "http://114.212.7.87/mobile";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mBtnQueryVersionModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<VersionModule> versionModules = VersionModuleDao
                        .getInstance(mActivity).queryForAll();
                AppLogger.e(versionModules.toString());
            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Module module = ModuleDao.getInstance(mActivity).queryForFirst();
                if (module != null) {
                    String json = GsonHelper.instance().toJson(module);
                    mTxtInfo.append("url:\n" + URL_PREX);
                    RequestParams params = new RequestParams();
                    params.put("json", json);
                    mTxtInfo.append("\nparams:\n" + params.toString());
                    post(URL_PREX, params, new SimpleHttpResponseHandler() {
                        @Override
                        public boolean handleJson(String json) {
                            mTxtInfo.append("\nresult:\n" + json);
                            return true;
                        }
                    });

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", "1");
                    map.put("name", "sam");
                    map.put("age", "31");
                    String json2 = GsonHelper.instance().toJson(map);
                    mTxtInfo.append("\njson:\n" + json2);
                }
            }
        });
    }
}
