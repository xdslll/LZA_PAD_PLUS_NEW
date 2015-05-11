package com.lza.pad.ui.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.lza.pad.R;
import com.lza.pad.helper.Consts;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;
import lza.com.lza.library.http.HttpUtility;
import lza.com.lza.library.util.AppLogger;
import lza.com.lza.library.util.Utility;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/6/15.
 */
public abstract class BaseSlidingActivity extends SlidingFragmentActivity implements Consts.ParamKey {

    protected Activity mCtx;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 通用的加载样式
     */
    private ViewStub mViewStubLoading = null;
    private LinearLayout mLayoutLoading = null;
    private TextView mTxtLoadingText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected ProgressDialog createProgressDialog(String msg, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(mCtx);
        dialog.setIndeterminate(true);
        dialog.setCancelable(cancelable);
        dialog.setMessage(msg);
        return dialog;
    }

    protected ProgressDialog createProgressDialog(String msg) {
        return createProgressDialog(msg, true);
    }

    protected void send(String url, AsyncHttpResponseHandler handler) {
        HttpUtility httpUtility = new HttpUtility(mCtx, HttpUtility.ASYNC_HTTP_CLIENT);
        httpUtility.send(url, handler);
    }

    protected String wrap(String value, String defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : value;
    }

    protected int parseInt(String value) {
        return Utility.safeIntParse(value, 0);
    }

    protected int parseInt(String value, int defaultValue) {
        return Utility.safeIntParse(value, defaultValue);
    }

    protected boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    protected <T> boolean isEmpty(List<T> data) {
        return data == null || data.size() <= 0;
    }

    protected <T> T pickFirst(List<T> data) {
        if (isEmpty(data)) return null;
        return data.get(0);
    }

    protected  <T> void clear(List<T> data) {
        if (isEmpty(data)) return;
        data.clear();
    }

    protected void log(String msg) {
        AppLogger.e("---------------- " + msg + " ----------------");
    }

    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    protected void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    protected boolean isRegisterEventBus() {
        return EventBus.getDefault().isRegistered(this);
    }

    protected Handler getMainHandler() {
        return mHandler;
    }

    protected void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    protected String buildCodePath(String activityPath) {
        String packageName = getPackageName();
        StringBuffer buffer = new StringBuffer();
        buffer.append(packageName).append(".").append(activityPath);
        return buffer.toString();
    }

    protected boolean isTopActivity() {
        String currentActivityName = getClass().getSimpleName();
        String topActivityName = getTopActivityName();
        boolean isTopActivity = currentActivityName.equals(topActivityName);
        log("是否为Top Activity：" + isTopActivity);
        return isTopActivity;
    }

    protected String getTopActivityName() {
        ActivityManager manager = (ActivityManager) mCtx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        if (runningTasks == null || runningTasks.size() == 0) return null;
        String className = runningTasks.get(0).topActivity.getShortClassName();
        if (TextUtils.isEmpty(className) || !className.contains(".")) return null;
        int index = className.lastIndexOf(".");
        return className.substring(index + 1, className.length());
    }

    protected void launchFragment(Fragment fragment, int id) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.commit();
    }

    /**
     * 显示Loading进度条
     *
     */
    protected void showLoadingView() {
        if (mLayoutLoading == null) {
            mViewStubLoading = (ViewStub) findViewById(R.id.common_viewstub);
            mViewStubLoading.inflate();
            mLayoutLoading = (LinearLayout) findViewById(R.id.common_loading_layout);
            mTxtLoadingText = (TextView) findViewById(R.id.common_loading_layout_text);
            mLayoutLoading.setVisibility(View.VISIBLE);
        } else {
            mLayoutLoading.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏Loading进度条
     */
    protected void dismissLoadingView() {
        if (mLayoutLoading != null) {
            mLayoutLoading.setVisibility(View.GONE);
        }
    }

    /**
     * 设置Loading文本
     *
     * @param text
     */
    protected void setLoadingViewText(String text) {
        if (mTxtLoadingText != null) {
            mTxtLoadingText.setText(text);
        }
    }

    /**
     * 处理失败信息
     *
     * @param title
     * @param message
     * @param runnable
     */
    protected void handleErrorProcess(int title, int message, final Runnable runnable) {
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(mCtx)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.dialog_button_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (runnable != null) {
                                getMainHandler().post(runnable);
                            }
                        }
                    })
                    .setNegativeButton(R.string.dialog_button_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }
}
