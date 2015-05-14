package com.lza.pad.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Window;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.dao.UserDao;
import com.lza.pad.db.model.ResponseData;
import com.lza.pad.db.model.School;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.handler.SimpleHttpResponseHandler;
import com.lza.pad.helper.JsonParseHelper;
import com.lza.pad.helper.Settings;
import com.lza.pad.helper.UrlHelper;
import com.lza.pad.ui.base.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/6/15.
 */
public class SchoolListActivity extends BaseActivity {

    ListView mSchoolList;
    QuickAdapter<SchoolVersion> mAdapter;
    List<SchoolVersion> mDataSource;

    TextView mTxtWelcome;

    String mSchoolBh, mSchoolName;

    private static final int DEFAULT_DELAY = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (checkConfig()) {
            showWelcomePage();
        } else {
            setContentView(R.layout.school_list);
            mSchoolList = (ListView) findViewById(R.id.school_list);
            checkUser();
        }
    }

    private void showWelcomePage() {
        setContentView(R.layout.school_welcome);
        mTxtWelcome = (TextView) findViewById(R.id.school_welcome_text);
        mTxtWelcome.setText(mSchoolName);
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        }, DEFAULT_DELAY);
    }

    private void jumpToLoginWithSchoolBh() {
        Intent intent = new Intent(mCtx, LoginActivity.class);
        intent.putExtra(KEY_SCHOOL_BH, mSchoolBh);
        intent.putExtra(KEY_SCHOOL_NAME, mSchoolName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void setConfig(SchoolVersion schoolVersion) {
        if (schoolVersion != null) {
            School school = pickFirst(schoolVersion.getSchool_bh());
            if(school != null) {
                mSchoolBh = school.getBh();
                mSchoolName = school.getTitle();
                Settings.setSchoolBh(mCtx, mSchoolBh);
                Settings.setSchoolName(mCtx, mSchoolName);
            }
        }
    }

    private boolean checkConfig() {
        Properties properties = new Properties();
        try {
            InputStream is = getResources().openRawResource(R.raw.config);
            properties.load(is);
            mSchoolBh = (String) properties.get(KEY_SCHOOL_BH);
            mSchoolName = (String) properties.get(KEY_SCHOOL_NAME);
            is.close();
            if (isEmpty(mSchoolBh)) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkUser() {
        UserDao userDao = new UserDao(mCtx);
        User user = userDao.queryForFirst();
        if (user != null) {
            Intent intent = new Intent(mCtx, LoginActivity.class);
            intent.putExtra(KEY_USER, user);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            finish();

            overridePendingTransition(0, 0);
            startActivity(intent);
        } else {
            mSchoolBh = Settings.getSchoolBh(mCtx);
            mSchoolName = Settings.getSchoolName(mCtx);
            if (!isEmpty(mSchoolBh)) {
                jumpToLoginWithSchoolBh();
            } else {
                requestSchoolVersion();
            }
        }
    }

    private void requestSchoolVersion() {
        if (isEmpty(mSchoolBh)) {
            showLoadingView();
            String url = UrlHelper.getSchoolVersion();
            send(url, new SchoolVersionHandler());
        } else {
            jumpToLoginWithSchoolBh();
        }
    }

    private class SchoolVersionHandler extends SimpleHttpResponseHandler<SchoolVersion> {

        @Override
        public ResponseData<SchoolVersion> parseJson(String json) {
            return JsonParseHelper.parseSchoolVersion(json);
        }

        @Override
        public void handleRespone(List<SchoolVersion> content) {
            dismissLoadingView();
            showList(content);
        }

        @Override
        public void handleResponseFailed(Object... obj) {
            dismissLoadingView();
            handleErrorProcess(R.string.school_list_request_failed_title,
                    R.string.school_list_request_failed_message,
                    new Runnable() {
                        @Override
                        public void run() {
                            requestSchoolVersion();
                        }
                    });
        }
    }

    private void showList(List<SchoolVersion> content) {
        mDataSource = content;
        if (mAdapter == null) {
            mAdapter = new QuickAdapter<SchoolVersion>(mCtx, R.layout.school_list_item, mDataSource) {
                @Override
                protected void convert(BaseAdapterHelper baseAdapterHelper, SchoolVersion schoolVersion) {
                    if (!SchoolListActivity.this.isEmpty(schoolVersion.getSchool_bh())) {
                        School school = schoolVersion.getSchool_bh().get(0);
                        baseAdapterHelper.setText(R.id.school_list_item_text, school.getTitle());

                    } else {
                        baseAdapterHelper.setText(R.id.school_list_item_text, "");
                    }
                }
            };
            mSchoolList.setAdapter(mAdapter);
            mSchoolList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SchoolVersion schoolVersion = mDataSource.get(position);
                    setConfig(schoolVersion);
                    Intent intent = new Intent(mCtx, LoginActivity.class);
                    intent.putExtra(KEY_SCHOOL_VERSION, schoolVersion);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

}
