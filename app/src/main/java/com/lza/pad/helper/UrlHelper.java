package com.lza.pad.helper;

import android.text.TextUtils;

import com.lza.pad.db.model.Config;
import com.lza.pad.db.model.ConfigGroup;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.School;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.db.model.Version;
import com.lza.pad.db.model.VersionModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lza.com.lza.library.http.UrlRequest;
import lza.com.lza.library.util.AppLogger;
import lza.com.lza.library.util.Utility;

/**
 * 业务类，用于生成Url和Url参数
 *
 * @author xiads
 * @Date 15/1/22.
 */
public class UrlHelper implements UrlParams, Consts.Normal {

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

    public static String getAllVersionModule(Version version) {
        Map<String, String> par = new HashMap<String, String>();
        par.put(PAR_CONTROL, CONTROL_GET_ALL_VERSION_MODULE);
        par.put(PAR_VERSION_ID, version.getId());
        return generateUrl(par);
    }

    public static String getVersionModule(Version version, String moduleType) {
        Map<String, String> par = new HashMap<String, String>();
        par.put(PAR_CONTROL, CONTROL_GET_VERSION_MODULE);
        par.put(PAR_VERSION_ID, version.getId());
        par.put(PAR_TYPE, moduleType);
        return generateUrl(par);
    }

    public static String getLoginVersionModule(Version version) {
        return getVersionModule(version, VersionModule.MODULE_TYPE_LOGIN);
    }

    public static String getSchoolVersionByBh(String schoolBh) {
        Map<String, String> par = new HashMap<String, String>();
        par.put(PAR_CONTROL, CONTROL_GET_SCHOOL_VERSION_BY_BH);
        par.put(PAR_SCHOOL_BH, schoolBh);
        return generateUrl(par);
    }

    public static String getConfigs(ConfigGroup configGroup) {
        Map<String, String> par = createParams(
                PAR_CONTROL, CONTROL_GET_CONFIGS,
                PAR_CONFIG_GROUP_ID, configGroup.getId());
        return generateUrl(par);
    }

    /**
     * 根据传入的数组创建参数
     *
     * @param params
     * @return
     */
    public static Map<String, String> createParams(String... params) {
        Map<String, String> mapPar = new HashMap<String, String>();
        for (int i = 0; i < params.length; i += 2) {
            mapPar.put(params[i], params[i + 1]);
        }
        return mapPar;
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

    public static UrlRequest buildUrl(SchoolVersion schoolVersion, Module module, List<Config> configList, User user) {
        /*
         * SchoolVersion和Module对象不能为NULL
         */
        if (schoolVersion == null || module == null) {
            return null;
        }
        UrlRequest request = new UrlRequest();

        /*
         * 封装URL地址
         */
        String method = module.getUrl();
        if(method.startsWith(HTTP_PROTOCOL)) {
            request.setUrl(method);
        } else {
            String url = schoolVersion.getUrl();
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
        }
        /*
         * 封装URL参数
         */
        if (!Utility.isEmpty(configList)) {
            Map<String, String> params = new HashMap<String, String>();
            for (Config config : configList) {
                String key = config.getKey();
                String value = config.getValue();
                //如果参数带有{}，则进行转义
                if (UrlRequest.needParseValue(value)) {
                    value = UrlRequest.parseValue(value);
                    if (value.equals(PAR_SCHOOL_BH)) {
                        //转义SCHOOL_BH
                        if (!Utility.isEmpty(schoolVersion.getSchool_bh())) {
                            School school = schoolVersion.getSchool_bh().get(0);
                            if (school != null) {
                                value = school.getBh();
                            } else {
                                value = EMPTY_VALUE;
                            }
                        }
                    } else if (value.equals(PAR_MODULE_ID)) {
                        //转义MODULE_ID
                        value = module.getId();
                    } else if (value.equals(PAR_USERNAME)) {
                        //转义USERNAME
                        if (user != null) {
                            value = user.getUsername();
                        } else {
                            value = EMPTY_VALUE;
                        }
                    } else if (value.equals(PAR_PASSWORD)) {
                        //转义PASSWORD
                        if (user != null) {
                            value = user.getPassword();
                        } else {
                            value = EMPTY_VALUE;
                        }
                    }
                }
                if (value == null) {
                    value = EMPTY_VALUE;
                }
                params.put(key, value);
            }
            request.setParams(params);
        }

        AppLogger.e(request.toString());
        return request;
    }

}
