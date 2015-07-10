package com.lza.pad.ui.main;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
 *
 * Created by lansing on 2015/5/26.
 */
public class WebViewActivity extends BaseActivity {
    private static final int LOADER_ID = 3;

    // data
    private Module mModuleInfo = null;
    private SchoolVersion mSchoolVersion = null;
    private User mUser = null;

    private List<Config> mModuleConfigs = new ArrayList<Config>();

    // actionbar
    private TextView mTitleTxt = null;

    // controls
    private WebView mWebView = null;
    private WebSettings mWebSettings = null;

    private boolean isInit = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        Bundle mBundle = getIntent().getBundleExtra("data");
        mModuleInfo = mBundle.getParcelable("module");
        mSchoolVersion = mBundle.getParcelable("schoolVersion");
        mUser = mBundle.getParcelable("user");

        initActionBar();
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

    private void initView() {
        setContentView(R.layout.webview_activity_layout);
        mWebView = (WebView) findViewById(R.id.webview_activity_web);
    }

    private void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        log(" mActionBar = " + mActionBar);
        if( null == mActionBar )
            return;

        //mActionBar.setDisplayShowTitleEnabled(true);
        //mActionBar.setTitle(mModuleInfo.getName());

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.action_bar_title_layout);

        mTitleTxt = (TextView) findViewById(R.id.action_bar_title);
        mTitleTxt.setText(mModuleInfo.getName());

        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.action_bar_more_menu ,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            if ( null != mWebView && mWebView.canGoBack()) {
                mWebView.goBack();
            }else{
                WebViewActivity.this.finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            if( null != mModuleConfigs ){
                mModuleConfigs.clear();
            }

            if( !isEmpty(data) ){
                mModuleConfigs.addAll(data);
            }

            initWebView(buildLoginUrl());
        }

        @Override
        public void onLoaderReset(Loader<List<Config>> loader) {
            loader.forceLoad();
        }
    }

    private void initWebView(UrlRequest request) {
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new LoginWebViewClient());
        mWebView.setWebChromeClient(new LoginWebChromeClient());

        //mWebView.addJavascriptInterface(new InJavaScript(this), "injs");

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


    // 组装模块请求地址和参数
    private UrlRequest buildLoginUrl() {
        return UrlHelper.buildUrl(mSchoolVersion, mModuleInfo, mModuleConfigs, mUser);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
            if ( null != mWebView && mWebView.canGoBack() ) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
