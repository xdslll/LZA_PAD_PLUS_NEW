package com.lza.pad.helper;

import android.text.TextUtils;

import com.lza.pad.db.model.Version;

import java.util.HashMap;
import java.util.Map;

import lza.com.lza.library.util.Utility;

/**
 * 业务类，用于生成Url和Url参数
 *
 * @author xiads
 * @Date 15/1/22.
 */
public class UrlHelper implements UrlParams {

    public static String generateUrl(Map<String, String> par) {
        String param = Utility.encodeUrl(par);
        StringBuilder builder = new StringBuilder();
        builder.append(DEFAULT_URL).append(param);
        return builder.toString();
    }

    public static String getSchoolVersion() {
        Map<String, String> par = new HashMap<String, String>();
        par.put(PAR_CONTROL, CONTROL_GET_SCHOOL_VERSION);
        return generateUrl(par);
    }

    public static String getVersionModule(Version version) {
        Map<String, String> par = new HashMap<String, String>();
        par.put(PAR_CONTROL, CONTROL_GET_VERSION_MODULE);
        par.put(PAR_VERSION_ID, version.getId());
        return generateUrl(par);
    }

    /**
     * 解析Url中的control参数
     *
     * @param url
     * @return
     */
    public static String parseControl(String url) {
        String control = "";
        if (TextUtils.isEmpty(url)) return control;
        int index = url.indexOf("control=");
        if (index > 0) {
            control = url.split("control=")[1];
            if (!TextUtils.isEmpty(control))
                control = control.split("&")[0];
        }
        return control;
    }


}
