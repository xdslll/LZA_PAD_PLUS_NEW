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

import lza.com.lza.library.util.Utility;

/**
 * Say something about this class
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

    WebView mWebView;
    WebSettings mWebSettings;

    boolean mClearMenuVisibility = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mUser = getIntent().getParcelableExtra(KEY_USER);
        if (mUser == null) {
            mSchoolVersion = getIntent().getParcelableExtra(KEY_SCHOOL_VERSION);
            if (mSchoolVersion == null) {
                mSchoolBh = getIntent().getStringExtra(KEY_SCHOOL_BH);
                mSchoolName = getIntent().getStringExtra(KEY_SCHOOL_NAME);
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

        mWebView.addJavascriptInterface(new InJavaScript(), "injs");
        mWebView.loadUrl(url);
    }

    private String createUrl() {
        //String url = "http://www.d1bu.me/mobile/login?school_bh=" + mSchool.getBh();
        String url = "";
        if (mUser != null) {
            url = "http://192.168.1.114:8080/mobile/login?school_bh=" + mUser.getSchool_bh() + "&username=" + mUser.getUsername() + "&password=" + mUser.getPassword();
        } else if (mSchool != null) {
            url = "http://192.168.1.114:8080/mobile/login?school_bh=" + mSchool.getBh();
        } else if (!isEmpty(mSchoolBh)) {
            url = "http://192.168.1.114:8080/mobile/login?school_bh=" + mSchoolBh;
        }
        log("url=" + url);
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

    public final class InJavaScript {

        @JavascriptInterface
        public void login(final String username, final String password, final String sessionValue, final String schoolBh) {
            getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    jump(username, password, sessionValue, schoolBh);
                }
            });
        }

        @JavascriptInterface
        public void skip() {
            skipLogin();
        }

        public void jump(final String username, final String password, final String sessionValue, final String schoolBh) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setSession(sessionValue);
            user.setSchool_bh(schoolBh);
            UserDao userDao = new UserDao(mCtx);
            Dao.CreateOrUpdateStatus status = userDao.createOrUpdate(user);
            if (status != null) {
                if (status.isCreated()) {
                    log("User新增成功");
                } else if (status.isUpdated()) {
                    log("User更新成功");
                } else {
                    log("User新增或更新失败");
                }
            }
            Intent intent = new Intent(mCtx, MainActivity.class);
            intent.putExtra(KEY_USER, user);
            if (mSchoolVersion != null) {
                intent.putExtra(KEY_SCHOOL_VERSION, mSchoolVersion);
            }
            startActivity(intent);
            finish();
        }

        @JavascriptInterface
        public void hideClearMenu() {
            mClearMenuVisibility = false;
            invalidateOptionsMenu();
        }

        @JavascriptInterface
        public void showClearMenu() {
            mClearMenuVisibility = true;
            invalidateOptionsMenu();
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
                        Intent intent = new Intent(mCtx, SchoolListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        overridePendingTransition(0, 0);
                        finish();

                        overridePendingTransition(0, 0);
                        startActivity(intent);
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
        finish();

        startActivity(intent);
    }

    private boolean checkIfSkipLogin() {
        return Settings.getIfSkipLogin(mCtx);
    }
}
