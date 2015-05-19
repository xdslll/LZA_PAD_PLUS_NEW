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
import com.lza.pad.db.model.Config;
import com.lza.pad.db.model.ConfigGroup;
import com.lza.pad.db.model.Configs;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.ResponseData;
import com.lza.pad.db.model.School;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.db.model.Version;
import com.lza.pad.db.model.VersionModule;
import com.lza.pad.handler.SimpleHttpResponseHandler;
import com.lza.pad.helper.JsonParseHelper;
import com.lza.pad.helper.Settings;
import com.lza.pad.helper.UrlHelper;
import com.lza.pad.ui.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import lza.com.lza.library.http.UrlRequest;
import lza.com.lza.library.util.ToastUtils;
import lza.com.lza.library.util.Utility;

/**
 * 前置条件：
 * KEY_IF_SKIP_LOGIN - 是否跳过登录（从配置文件中读取，默认为false）
 *
 * 输入：
 *  KEY_USER - 已登录用户（可以为空）
 *  KEY_SCHOOL_VERSION - 学校版本信息（不能为空，如果为空，将返回选择学校列表界面）
 *
 *  此外还需要传入用途
 *      KEY_LOGIN_USAGE - 页面的用途，默认为0，表示成功后跳转到主页面，如果为1则表示登录成功后关闭当前页面
 *
 * 输出：
 *  KEY_USER
 *  KEY_SCHOOL_VERSION
 *
 * 跳转：
 *  1、跳转到主页面
 *  2、关闭本页面，返回OK
 *  setResult(Result.OK);
 *
 * @author xiads
 * @Date 5/7/15.
 */
public class LoginActivity extends BaseActivity {

    User mUser;

    SchoolVersion mSchoolVersion;
    School mSchool;
    Version mVersion;

    int mLoginUsage;

    WebView mWebView;
    WebSettings mWebSettings;

    boolean mClearMenuVisibility = true;

    Module mLoginModule;
    ConfigGroup mLoginConfigGroup;
    List<Config> mLoginConfigs = new ArrayList<Config>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /**
         * [101]获取SchoolVersion
         */
        log("[101]获取SchoolVersion");
        mSchoolVersion = getIntent().getParcelableExtra(KEY_SCHOOL_VERSION);

        //先检查SchoolVersion对象是否为NULL，如果为NULL，则返回选择学校页面重新选择
        if (checkSchoolVersion()) {
            mLoginUsage = getIntent().getIntExtra(KEY_LOGIN_USAGE, DEFAULT_LOGIN);
            mUser = getIntent().getParcelableExtra(KEY_USER);
            /**
             * [104]是否存在登录用户
             */
            log("[104]是否存在登录用户");
            if (mUser == null) {
                if (checkIfSkipLogin()) {
                    //没登录时可以跳过登录
                    skipLogin();
                } else {
                    //已有登录信息后会进行自动跳转
                    initView();
                }
            } else {
                //如果已有登录，必须重新登录刷新Session
                initView();
            }
        }
    }

    /**
     * [107]初始化UI
     */
    private void initView() {
        log("[107]初始化UI");
        setContentView(R.layout.common_webview);
        mWebView = (WebView) findViewById(R.id.webview);
        requestLoginModule();
    }

    /**
     * [108]请求登录模块
     */
    private void requestLoginModule() {
        log("[108]请求登录模块");
        showLoadingView();
        String url = UrlHelper.getLoginVersionModule(mVersion);
        send(url, new VersionModuleHandler());
    }

    /**
     * 处理登录模块数据
     */
    private class VersionModuleHandler extends SimpleHttpResponseHandler<VersionModule> {

        @Override
        public ResponseData<VersionModule> parseJson(String json) {
            return JsonParseHelper.parseVersionModule(json);
        }

        @Override
        public void handleRespone(List<VersionModule> content) {
            dismissLoadingView();
            VersionModule versionModule = content.get(0);
            mLoginModule = versionModule != null ? pickFirst(versionModule.getModule_id()) : null;
            /**
             * [109]检查登录模块是否存在参数
             */
            mLoginConfigGroup = versionModule != null ? pickFirst(versionModule.getConfig_group_id()) : null;
            if (mLoginConfigGroup != null) {
                requestConfig();
            } else {
                if (mLoginModule != null) {
                    initWebView(buildLoginUrl());
                } else {
                    showErrorDialog();
                }
            }
        }

        @Override
        public void handleResponseFailed(Object... obj) {
            dismissLoadingView();
            showBadConnectionDialog();
        }
    }

    /**
     * [110]请求登录模块参数
     */
    private void requestConfig() {
        showLoadingView();
        String url = UrlHelper.getConfigs(mLoginConfigGroup);
        send(url, new LoginConfigHandler());
    }

    /**
     * 处理登录配置数据
     */
    private class LoginConfigHandler extends SimpleHttpResponseHandler<Configs> {

        @Override
        public ResponseData<Configs> parseJson(String json) {
            return JsonParseHelper.parseConfigs(json);
        }

        @Override
        public void handleRespone(List<Configs> content) {
            dismissLoadingView();
            for (Configs configs : content) {
                if (!isEmpty(configs.getConfig_id())) {
                    mLoginConfigs.add(pickFirst(configs.getConfig_id()));
                }
            }
            initWebView(buildLoginUrl());
        }

        @Override
        public void handleResponseFailed(Object... obj) {
            dismissLoadingView();
            showBadConnectionDialog();
        }
    }

    /**
     * [111]组装登录模块请求地址和参数
     *
     * @return
     */
    private UrlRequest buildLoginUrl() {
        log("[111]组装登录模块请求地址和参数");
        return UrlHelper.buildUrl(mSchoolVersion, mLoginModule, mLoginConfigs, mUser);
    }

    /**
     * [102]检查SchoolVersion是否为NULL
     *
     * @return
     */
    private boolean checkSchoolVersion() {
        log("[102]检查SchoolVersion是否为NULL");
        //如果SchoolVersion对象为空，返回选择学校页面
        if (mSchoolVersion == null) {
            showSchoolEmptyDialog();
            return false;
        } else {
            //判断SchoolVersion对象的url、学校编号和版本号是否为空，如果有一项为空，就返回选择学校页面
            if (isEmpty(mSchoolVersion.getUrl()) ||
                    isEmpty(mSchoolVersion.getSchool_bh()) ||
                    isEmpty(mSchoolVersion.getVersion_code())) {
                showSchoolEmptyDialog();
                return false;
            } else {
                //获取School对象和Version对象，School对象用于获取学校编号，Version对象用于获取登录模块信息
                mSchool = pickFirst(mSchoolVersion.getSchool_bh());
                mVersion = pickFirst(mSchoolVersion.getVersion_code());
                return true;
            }
        }
    }

    /**
     * [103]返回选择学校界面
     */
    private void showSchoolEmptyDialog() {
        log("[103]返回选择学校界面");
        Utility.showDialog(mCtx, R.string.dialog_prompt_title,
                R.string.dialog_confirm_back_to_list,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backToSelectSchoolList();
                    }
                });
    }

    /**
     * 网络连接失败时提示
     */
    private void showBadConnectionDialog() {
        handleErrorProcess(R.string.dialog_prompt_title,
                R.string.dialog_request_failed_message,
                new Runnable() {
                    @Override
                    public void run() {
                        requestLoginModule();
                    }
                });
    }

    /**
     * 返回学校选择界面
     */
    private void backToSelectSchoolList() {
        Intent intent = new Intent(mCtx, SchoolListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    /**
     * [112]初始化WebView
     *
     * @param request
     */
    private void initWebView(UrlRequest request) {
        log("[112]初始化WebView");
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new LoginWebViewClient());
        mWebView.setWebChromeClient(new LoginWebChromeClient());

        mWebView.addJavascriptInterface(new InJavaScript(this), "injs");
        //mWebView.loadUrl(url);
        log(new String(request.getPostData()));
        mWebView.postUrl(request.getUrl(), request.getPostData());
    }

    /**
     * 点击返回键时优先执行WebView回退
     *
     * @param keyCode
     * @param event
     * @return
     */
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
            UserDao userDao = UserDao.getInstance(activity);
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

    /**
     * [106]跳过登录
     */
    private void skipLogin() {
        log("[106]跳过登录");
        Settings.setIfSkipLogin(mCtx, true);
        if (mLoginUsage == DEFAULT_LOGIN) {
            Intent intent = new Intent(mCtx, MainActivity.class);
            if (mSchoolVersion != null) {
                intent.putExtra(KEY_SCHOOL_VERSION, mSchoolVersion);
            }
            if (mUser != null) {
                intent.putExtra(KEY_USER, mUser);
            }
            startActivity(intent);
        } else {
            setResult(RESULT_OK);
        }
        finish();
    }

    /**
     * [105]是否需要跳过登录
     *
     * @return
     */
    private boolean checkIfSkipLogin() {
        log("[105]是否需要跳过登录");
        return Settings.getIfSkipLogin(mCtx);
    }
}
/*private UrlRequest buildLoginUrl() {
        UrlRequest request = new UrlRequest();

        String url = mSchoolVersion.getUrl();
        String method = mLoginModule.getUrl();
        StringBuilder builder = new StringBuilder();
        if (!url.startsWith(HTTP_PROTOCOL)) {
            builder.append(HTTP_PROTOCOL);
        }
        builder.append(url);
        if (!url.endsWith(SEPARATOR)) {
            builder.append(SEPARATOR);
        }
        if (method.startsWith(SEPARATOR)) {
            method = method.substring(1, method.length());
        }
        builder.append(method);
        request.setUrl(builder.toString());

        if (!isEmpty(mLoginConfigs)) {
            Map<String, String> params = new HashMap<String, String>();
            for (Config config : mLoginConfigs) {
                String key = config.getKey();
                String value = config.getValue();
                if (UrlRequest.needParseValue(value)) {
                    value = UrlRequest.parseValue(value);
                    if (value.equals(PAR_SCHOOL_BH)) {
                        value = mSchool.getBh();
                    } else if (value.equals(PAR_MODULE_ID)) {
                        value = mLoginModule.getId();
                    }
                }
                params.put(key, value);
            }
            request.setParams(params);
        }

        log(request.toString());
        return request;
    }*/
