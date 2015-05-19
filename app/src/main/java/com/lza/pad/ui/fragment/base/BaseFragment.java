package com.lza.pad.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lza.pad.helper.Consts;
import com.lza.pad.helper.ImageHelper;

import java.util.List;

import lza.com.lza.library.http.HttpUtility;
import lza.com.lza.library.util.AppLogger;
import lza.com.lza.library.util.Utility;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/12/15.
 */
public class BaseFragment extends Fragment implements Consts.ParamKey {

    protected FragmentActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    protected void send(String url, AsyncHttpResponseHandler handler) {
        HttpUtility httpUtility = new HttpUtility(mActivity, HttpUtility.ASYNC_HTTP_CLIENT);
        httpUtility.send(url, handler);
    }

    protected void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        HttpUtility httpUtility = new HttpUtility(mActivity, HttpUtility.ASYNC_HTTP_CLIENT);
        httpUtility.post(url, params, handler);
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

    protected ImageHelper getImageHelper() {
        return ImageHelper.getInstance(mActivity);
    }
}
