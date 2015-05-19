package com.lza.pad.helper;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/7/15.
 */
public interface Consts {

    public interface Normal {
        public static final String HTTP_PROTOCOL = "http://";
        public static final String SEPARATOR = "/";
        public static final String EMPTY_VALUE = "";
    }

    public interface ParamKey {
        public static final String KEY_SCHOOL_VERSION = "key_school_version";
        public static final String KEY_USER = "key_user";

        public static final String KEY_SCHOOL_BH = "school_bh";
        public static final String KEY_SCHOOL_NAME = "school_name";

        public static final String KEY_IF_SKIP_LOGIN = "key_if_skip_login";
        public static final String KEY_LOGIN_USAGE = "key_login_usage";

        /**
         * 系统正常流程登录
         */
        public static final int DEFAULT_LOGIN = 0;

        /**
         * 用户主动请求登录
         */
        public static final int CUSTOM_LOGIN = 1;
    }

}
