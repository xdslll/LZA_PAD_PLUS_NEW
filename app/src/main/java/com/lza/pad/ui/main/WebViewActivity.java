package com.lza.pad.ui.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lza.pad.R;
import com.lza.pad.db.loader.ConfigLoader;
import com.lza.pad.db.model.Config;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.helper.UrlHelper;
import com.lza.pad.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import lza.com.lza.library.http.UrlRequest;
import lza.com.lza.library.util.Utility;

/**
 * Created by lansing on 2015/5/26.
 */
public class WebViewActivity extends BaseActivity {
    private static final int LOADER_ID = 3;

    // data
    private Module mModuleInfo = null;
    private SchoolVersion mSchoolVersion = null;
    private User mUser = null;

    List<Config> mModuleConfigs = new ArrayList<Config>();

    // controls
    private WebView mWebView = null;
    WebSettings mWebSettings;

    private boolean isInit = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity_layout);

        Bundle mBundle = getIntent().getBundleExtra("data");
        mModuleInfo = mBundle.getParcelable("module");
        mSchoolVersion = mBundle.getParcelable("schoolVersion");
        mUser = mBundle.getParcelable("user");
    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();
        readDataFromDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView(){
        mWebView = (WebView) findViewById(R.id.webview_activity_web);
    }

    private void readDataFromDB(){
        if (isInit) {
            WebViewActivity.this.getSupportLoaderManager().initLoader(LOADER_ID, null, new configLoaderCallbacks());
            isInit = false;
        } else {
            WebViewActivity.this.getSupportLoaderManager().restartLoader(LOADER_ID, null, new configLoaderCallbacks());
        }
    }

    private class configLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Config>>{

        @Override
        public Loader<List<Config>> onCreateLoader(int id, Bundle args) {
            return new ConfigLoader(WebViewActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<List<Config>> loader, List<Config> data) {
            mModuleConfigs.addAll(data);

            initWebView(buildLoginUrl());
        }

        @Override
        public void onLoaderReset(Loader<List<Config>> loader) {
            loader.forceLoad();
        }
    }

    private void initWebView(UrlRequest request) {
        log("[112]初始化WebView");
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new LoginWebViewClient());
        mWebView.setWebChromeClient(new LoginWebChromeClient());

        //mWebView.addJavascriptInterface(new InJavaScript(this), "injs");
        //mWebView.loadUrl(url);

        mWebView.postUrl(request.getUrl(), request.getPostData());
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
                setLoadingViewText(getResources().getString(R.string.loading_text));
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
            //showErrorDialog();
        }
    }

    private class LoginWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
            Utility.showDialog(mCtx, getString(R.string.dialog_prompt_title), message);
            result.confirm();
            return true;
        }
    }


    private UrlRequest buildLoginUrl() {
        log("[111]组装登录模块请求地址和参数");
        return UrlHelper.buildUrl(mSchoolVersion, mModuleInfo, mModuleConfigs, mUser);
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
}
