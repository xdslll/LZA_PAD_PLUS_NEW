package com.lza.pad.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.j256.ormlite.dao.Dao;
import com.lza.pad.R;
import com.lza.pad.db.dao.UserDao;
import com.lza.pad.db.model.School;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.db.model.Version;
import com.lza.pad.helper.Settings;
import com.lza.pad.ui.base.BaseActivity;

import java.lang.ref.WeakReference;

import lza.com.lza.library.util.ToastUtils;
import lza.com.lza.library.util.Utility;

/**
 * 前置条件：
 * KEY_IF_SKIP_LOGIN - 是否跳过登录（从配置文件中读取，默认为false）
 *
 * 输入：
 *  需要传入以下三类参数的一类：
 *  第一类：
 *      KEY_USER - 已登录用户
 *  第二类：
 *      KEY_SCHOOL_VERSION - 学校版本信息
 *  第三类：
 *      KEY_SCHOOL_BH - 学校编号
 *      KEY_SCHOOL_NAME - 学校名称，可选
 *  如果参数不为以上三类中的任何一类，系统将从SharedPreferences里读取学校编号
 *
 *  此外还需要传入用途
 *      KEY_LOGIN_USAGE - 页面的用途，默认为0，表示成功后跳转到主页面，如果为1则表示登录成功后关闭当前页面
 *
 * 输出：
 *  1、跳转到主页面
 *  2、关闭本页面，返回OK
 *
 * @author xiads
 * @Date 5/7/15.
 */
public class LoginActivity extends BaseActivity {

    User mUser;

    SchoolVersion mSchoolVersion;
    School mSchool;
    Version mVersion;

    String mSchoolBh, mSchoolName;
    int mLoginUsage;

    WebView mWebView;
    WebSettings mWebSettings;

    boolean mClearMenuVisibility = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mLoginUsage = getIntent().getIntExtra(KEY_LOGIN_USAGE, DEFAULT_LOGIN);
        mUser = getIntent().getParcelableExtra(KEY_USER);
        if (mUser == null) {
            mSchoolVersion = getIntent().getParcelableExtra(KEY_SCHOOL_VERSION);
            if (mSchoolVersion == null) {
                mSchoolBh = getIntent().getStringExtra(KEY_SCHOOL_BH);
                mSchoolName = getIntent().getStringExtra(KEY_SCHOOL_NAME);
                if (isEmpty(mSchoolBh)) {
                    mSchoolBh = Settings.getSchoolBh(mCtx);
                }
                if (isEmpty(mSchoolName)) {
                    mSchoolName = Settings.getSchoolName(mCtx);
                }
            } else {
                mSchool = pickFirst(mSchoolVersion.getSchool_bh());
                mVersion = pickFirst(mSchoolVersion.getVersion_code());
                if (mSchool != null) {
                    mSchoolBh = mSchool.getBh();
                    mSchoolName = mSchool.getTitle();
                }
            }
        }

        if (!checkIfSkipLogin()) {
            String url = createUrl();
            if (isEmpty(url)) {
                showErrorDialog();
            } else {
                setContentView(R.layout.common_webview);
                mWebView = (WebView) findViewById(R.id.webview);
                initWebView(url);
            }
        } else {
            skipLogin();
        }
    }

    private void initWebView(String url) {
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new LoginWebViewClient());
        mWebView.setWebChromeClient(new LoginWebChromeClient());

        mWebView.addJavascriptInterface(new InJavaScript(this), "injs");
        mWebView.loadUrl(url);
    }

    //private static final String URL_PREX = "192.168.31.130";
    private static final String URL_PREX = "192.168.1.114";
    private String createUrl() {
        //String url = "http://www.d1bu.me/mobile/login?school_bh=" + mSchool.getBh();
        String url = "";
        if (mUser != null) {
            url = "http://" + URL_PREX + ":8080/mobile/login?school_bh=" + mUser.getSchool_bh() + "&username=" + mUser.getUsername() + "&password=" + mUser.getPassword();
        } else if (mSchool != null) {
            url = "http://" + URL_PREX + ":8080/mobile/login?school_bh=" + mSchool.getBh();
        } else if (!isEmpty(mSchoolBh)) {
            url = "http://" + URL_PREX + ":8080/mobile/login?school_bh=" + mSchoolBh;
        }
        //String url = "http://192.168.1.114:8080/mobile/test";
        return url;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            if (mWebView != null) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static final class InJavaScript {

        WeakReference<LoginActivity> mActivity;

        public InJavaScript(LoginActivity activity) {
            this.mActivity = new WeakReference<LoginActivity>(activity);
        }

        @JavascriptInterface
        public void login(final String username, final String password, final String sessionValue, final String schoolBh) {
            mActivity.get().getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    jump(username, password, sessionValue, schoolBh);
                }
            });
        }

        @JavascriptInterface
        public void skip() {
            mActivity.get().skipLogin();
        }

        public void jump(final String username, final String password, final String sessionValue, final String schoolBh) {
            LoginActivity activity = mActivity.get();
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setSession(sessionValue);
            user.setSchool_bh(schoolBh);
            UserDao userDao = new UserDao(activity);
            Dao.CreateOrUpdateStatus status = userDao.createOrUpdate(user);
            if (status != null) {
                if (status.isCreated()) {
                    activity.log("User新增成功");
                    ToastUtils.showShort(activity, R.string.login_success);
                } else if (status.isUpdated()) {
                    activity.log("User更新成功");
                    ToastUtils.showShort(activity, R.string.login_success);
                } else {
                    activity.log("User新增或更新失败");
                }
            }
            if (activity.mLoginUsage == DEFAULT_LOGIN) {
                Intent intent = new Intent(mActivity.get(), MainActivity.class);
                intent.putExtra(KEY_USER, user);
                if (activity.mSchoolVersion != null) {
                    intent.putExtra(KEY_SCHOOL_VERSION, activity.mSchoolVersion);
                }
                activity.startActivity(intent);
                activity.finish();
            } else {
                Intent data = new Intent();
                data.putExtra(KEY_USER, user);
                if (activity.mSchoolVersion != null) {
                    data.putExtra(KEY_SCHOOL_VERSION, activity.mSchoolVersion);
                }
                activity.setResult(RESULT_OK, data);
                activity.finish();
            }
        }

        @JavascriptInterface
        public void hideClearMenu() {
            mActivity.get().mClearMenuVisibility = false;
            mActivity.get().invalidateOptionsMenu();
        }

        @JavascriptInterface
        public void showClearMenu() {
            mActivity.get().mClearMenuVisibility = true;
            mActivity.get().invalidateOptionsMenu();
        }
    }

    private class LoginWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingView();
            if (mUser != null) {
                setLoadingViewText("正在登录...");
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissLoadingView();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            showErrorDialog();
        }
    }

    private void showErrorDialog() {
        Utility.showDialog(mCtx, R.string.dialog_prompt_title, R.string.dialog_loading_failed,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mLoginUsage == DEFAULT_LOGIN) {
                            Intent intent = new Intent(mCtx, SchoolListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            overridePendingTransition(0, 0);
                            finish();

                            overridePendingTransition(0, 0);
                            startActivity(intent);
                        } else {
                            finish();
                        }
                    }
                });
    }

    private class LoginWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
            Utility.showDialog(mCtx, getString(R.string.dialog_prompt_title), message);
            result.confirm();
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!mClearMenuVisibility) {
            //menu.clear();
        } else {
            //getSupportMenuInflater().inflate(R.menu.login, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.skip_login) {
            skipLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void skipLogin() {
        Settings.setIfSkipLogin(mCtx, true);
        if (mLoginUsage == DEFAULT_LOGIN) {
            Intent intent = new Intent(mCtx, MainActivity.class);
            if (mSchoolVersion != null) {
                intent.putExtra(KEY_SCHOOL_VERSION, mSchoolVersion);
            }
            if (mUser != null) {
                intent.putExtra(KEY_USER, mUser);
            }
            if (!isEmpty(mSchoolBh)) {
                intent.putExtra(KEY_SCHOOL_BH, mSchoolBh);
            }
            if (!isEmpty(mSchoolName)) {
                intent.putExtra(KEY_SCHOOL_NAME, mSchoolName);
            }
            startActivity(intent);
        } else {
            setResult(RESULT_OK);
        }
        finish();
    }

    private boolean checkIfSkipLogin() {
        return Settings.getIfSkipLogin(mCtx);
    }

}
