package com.lza.pad.ui.fragment.base;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lza.pad.R;
import com.lza.pad.helper.Consts;
import com.lza.pad.helper.ImageHelper;

import java.io.File;
import java.util.List;

import lza.com.lza.library.http.HttpUtility;
import lza.com.lza.library.util.AppLogger;
import lza.com.lza.library.util.ToastUtils;
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

    protected <T> void clear(List<T> data) {
        if (isEmpty(data)) return;
        data.clear();
    }

    protected void log(String msg) {
        AppLogger.e("---------------- " + msg + " ----------------");
    }

    protected ImageHelper getImageHelper() {
        return ImageHelper.getInstance(mActivity);
    }

    protected void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    protected void openFile(Context context, File file) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置intent的Action属性
            //intent.setAction(Intent.ACTION_VIEW);
            // 获取文件file的MIME类型
            String type = getMIMEType(file);
            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);
            // 跳转
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            ToastUtils.showShort(context, R.string.file_cannot_open);
        }
    }

    private String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "")
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    // 可以自己随意添加
    private String[][] MIME_MapTable = {
        // {后缀名，MIME类型}
        {".3gp", "video/3gpp"},
        {".apk", "application/vnd.android.package-archive"},
        {".asf", "video/x-ms-asf"},
        {".avi", "video/x-msvideo"},
        {".bin", "application/octet-stream"},
        {".bmp", "image/bmp"},
        {".c", "text/plain"},
        {".class", "application/octet-stream"},
        {".conf", "text/plain"},
        {".cpp", "text/plain"},
        {".doc", "application/msword"},
        {".docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
        {".xls", "application/vnd.ms-excel"},
        {".xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
        {".exe", "application/octet-stream"},
        {".gif", "image/gif"},
        {".gtar", "application/x-gtar"},
        {".gz", "application/x-gzip"},
        {".h", "text/plain"},
        {".htm", "text/html"},
        {".html", "text/html"},
        {".jar", "application/java-archive"},
        {".java", "text/plain"},
        {".jpeg", "image/jpeg"},
        {".jpg", "image/jpeg"},
        {".js", "application/x-javascript"},
        {".log", "text/plain"},
        {".m3u", "audio/x-mpegurl"},
        {".m4a", "audio/mp4a-latm"},
        {".m4b", "audio/mp4a-latm"},
        {".m4p", "audio/mp4a-latm"},
        {".m4u", "video/vnd.mpegurl"},
        {".m4v", "video/x-m4v"},
        {".mov", "video/quicktime"},
        {".mp2", "audio/x-mpeg"},
        {".mp3", "audio/x-mpeg"},
        {".mp4", "video/mp4"},
        {".mpc", "application/vnd.mpohun.certificate"},
        {".mpe", "video/mpeg"},
        {".mpeg", "video/mpeg"},
        {".mpg", "video/mpeg"},
        {".mpg4", "video/mp4"},
        {".mpga", "audio/mpeg"},
        {".msg", "application/vnd.ms-outlook"},
        {".ogg", "audio/ogg"},
        {".pdf", "application/pdf"},
        {".png", "image/png"},
        {".pps", "application/vnd.ms-powerpoint"},
        {".ppt", "application/vnd.ms-powerpoint"},
        {".pptx",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
        {".prop", "text/plain"},
        {".rc", "text/plain"},
        {".rmvb", "audio/x-pn-realaudio"},
        {".rtf", "application/rtf"},
        {".sh", "text/plain"},
        {".tar", "application/x-tar"},
        {".tgz", "application/x-compressed"},
        {".txt", "text/plain"}, //文本文件的编码不能为ANSI，否则中文出现乱码
        {".wav", "audio/x-wav"},
        {".wma", "audio/x-ms-wma"},
        {".wmv", "audio/x-ms-wmv"},
        {".wps", "application/vnd.ms-works"},
        {".xml", "text/plain"},
        {".z", "application/x-compress"},
        {".zip", "application/x-zip-compressed"},
        {"", "*/*"}
    };
}
