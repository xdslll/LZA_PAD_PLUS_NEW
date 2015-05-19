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
    TextView mTxtWelcome;
    QuickAdapter<SchoolVersion> mAdapter;
    //List<SchoolVersion> mDataSource;
    String mSchoolBh, mSchoolName;

    private static final int DEFAULT_DELAY = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        init();
    }

    private void init() {
        if (checkConfig()) {
            showWelcomePage();
        } else {
            setContentView(R.layout.school_list);
            mSchoolList = (ListView) findViewById(R.id.school_list);

            mSchoolBh = Settings.getSchoolBh(mCtx);
            if (isEmpty(mSchoolBh)) {
                requestSchoolVersionList();
            } else {
                requestVersionBySchoolBh();
            }
        }
    }

    /**
     * 检查R.raw.config文件中是否已经定义学校信息
     *
     * @return
     */
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

    /**
     * 如果预置了学校信息，则跳过学校显示界面，直接加载school_welcome布局
     */
    private void showWelcomePage() {
        setContentView(R.layout.school_welcome);
        mTxtWelcome = (TextView) findViewById(R.id.school_welcome_text);
        mTxtWelcome.setText(mSchoolName);
        getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestVersionBySchoolBh();
            }
        }, DEFAULT_DELAY);
    }

    /**
     * 通过学校编号获取学校对应的版本信息
     */
    private void requestVersionBySchoolBh() {
        showLoadingView();
        String url = UrlHelper.getSchoolVersionByBh(mSchoolBh);
        send(url, new SingleSchoolVersionHanlder());
    }

    /**
     * 处理学校版本信息
     */
    private class SingleSchoolVersionHanlder extends SimpleHttpResponseHandler<SchoolVersion> {
        @Override
        public ResponseData<SchoolVersion> parseJson(String json) {
            return JsonParseHelper.parseSchoolVersion(json);
        }

        @Override
        public void handleRespone(List<SchoolVersion> content) {
            dismissLoadingView();
            SchoolVersion schoolVersion = content.get(0);
            jumpToLogin(schoolVersion);
        }

        @Override
        public void handleResponseFailed(Object... obj) {
            dismissLoadingView();
            handleErrorProcess(R.string.dialog_request_failed_title,
                    R.string.version_info_request_failed_message,
                    new Runnable() {
                        @Override
                        public void run() {
                            requestVersionBySchoolBh();
                        }
                    });
        }
    }

    /**
     * 跳转到登录界面
     *
     * @param schoolVersion
     */
    private void jumpToLogin(SchoolVersion schoolVersion) {
        //如果schoolVersion为Null则跳转回主流程
        if (schoolVersion == null) {
            init();
            return;
        }
        setConfig(schoolVersion);
        Intent intent = new Intent(mCtx, LoginActivity.class);
        intent.putExtra(KEY_SCHOOL_VERSION, schoolVersion);
        //如果存在登录用户，则传到Login界面
        User user = UserDao.getLoginUser(mCtx);
        if (user != null) {
            intent.putExtra(KEY_USER, user);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);

    }

    /**
     * 将学校编号信息保存到配置文件中
     *
     * @param schoolVersion
     */
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

    /**
     * 请求学校列表
     */
    private void requestSchoolVersionList() {
        showLoadingView();
        String url = UrlHelper.getSchoolVersion();
        send(url, new SchoolVersionListHandler());
    }

    /**
     * 处理学校版本列表
     */
    private class SchoolVersionListHandler extends SimpleHttpResponseHandler<SchoolVersion> {

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
            handleErrorProcess(R.string.dialog_request_failed_title,
                    R.string.school_list_request_failed_message,
                    new Runnable() {
                        @Override
                        public void run() {
                            requestSchoolVersionList();
                        }
                    });
        }
    }

    /**
     * 显示学校列表
     *
     * @param content
     */
    private void showList(final List<SchoolVersion> content) {
        if (mAdapter == null) {
            mAdapter = new QuickAdapter<SchoolVersion>(mCtx, R.layout.school_list_item, content) {
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
                    jumpToLogin(content.get(position));
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Deprecated
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

    @Deprecated
    private void checkUser() {
        User user = UserDao.getLoginUser(mCtx);
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
                requestSchoolVersionList();
            }
        }
    }

}
