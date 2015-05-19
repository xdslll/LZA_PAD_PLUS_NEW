package com.lza.pad.handler;

import android.text.TextUtils;

import com.loopj.android.http.TextHttpResponseHandler;
import com.lza.pad.db.model.ResponseData;
import com.lza.pad.helper.JsonParseHelper;

import org.apache.http.Header;

import java.util.List;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/4/15.
 */
public class SimpleHttpResponseHandler<T> extends TextHttpResponseHandler {

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        handleResponseFailed(statusCode, headers, responseString, throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String json) {
        if (TextUtils.isEmpty(json)) {
            onResponseDataEmpty();
        } else {
            //拦截Json的解析
            if (handleJson(json)) return;
            //解析Json
            ResponseData<T> data = parseJson(json);
            if (data == null) {
                onResponseParseFailed(json);
                return;
            } else {
                //拦截对响应数据的处理
                if (handleResponse(data)) return;
                //获取处理状态
                String state = data.getState();
                //如果响应状态不为1，则进行异常处理
                if (state == null || !state.equals(ResponseData.RESPONSE_STATE_OK)) {
                    onResponseStateError(data);
                    return;
                }
                //拦截状态响应成功时的情况
                if (handleResponseStatusOK(json)) return;
                //处理content字段为空的情况
                if (data.getContent() == null || data.getContent().size() <= 0) {
                    onResponseContentEmpty();
                    return;
                }
                //处理响应数据
                handleRespone(data.getContent());
            }
        }
    }


    @Override
    public void onRetry(int retryNo) {
        super.onRetry(retryNo);
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        super.onProgress(bytesWritten, totalSize);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

    @Override
    public void onCancel() {
        super.onCancel();
    }

    public void onResponseDataEmpty() {
        handleResponseFailed();
    }

    public boolean handleJson(String json) {
        return false;
    }

    public ResponseData<T> parseJson(String json) {
        try {
            return JsonParseHelper.parseSimpleResponse(json);
        } catch (Exception ex) {
            return null;
        }
    }

    public void onResponseParseFailed(String json) {
        handleResponseFailed(json);
    }

    public boolean handleResponse(ResponseData<T> data) {
        return false;
    }

    public void onResponseStateError(ResponseData<T> data) {
        handleResponseFailed(data);
    }

    public boolean handleResponseStatusOK(String json) {
        return false;
    }

    public void onResponseContentEmpty() {
        handleResponseFailed();
    }

    /**
     * 重要方法，处理响应结果，通常只需要重写这个方法即可
     *
     * @param content
     */
    public void handleRespone(List<T> content) {}

    /**
     * 重要方法，统一处理异常
     */
    public void handleResponseFailed(Object... obj) {}
}
