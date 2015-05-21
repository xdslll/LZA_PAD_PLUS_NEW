package lza.com.lza.library.http;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

import lza.com.lza.library.util.GsonHelper;

public class UrlRequest {

    String url;

    Map<String, String> params;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public RequestParams getRequestParams() {
        if (params != null) {
            RequestParams _params = new RequestParams();
            Set<Map.Entry<String, String>> sets = params.entrySet();
            for (Map.Entry<String, String> keySet : sets) {
                _params.put(keySet.getKey(), keySet.getValue());
            }
            return _params;
        } else {
            return null;
        }
    }

    public byte[] getPostData() {
        String json = GsonHelper.instance().toJson(params);
        try {
            json = URLDecoder.decode(json, "UTF-8");
            if (!TextUtils.isEmpty(json)) {
                try {
                    return ("json=" + json).getBytes();
                } catch (Exception ex) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean needParseValue(String value) {
        if (value.startsWith("{") &&
                value.endsWith("}")) {
            return true;
        } else {
            return false;
        }
    }

    public static String parseValue(String value) {
        return value.substring(1, value.length() - 1);
    }

    @Override
    public String toString() {
        return "UrlRequest{" +
                "url='" + url + '\'' +
                ", params=" + params +
                '}';
    }
}