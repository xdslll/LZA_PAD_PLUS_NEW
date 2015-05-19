package com.lza.pad.helper;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/7/15.
 */
public interface UrlParams {

    /**
     * 默认URL
     */
    public static String DEFAULT_URL = "http://pad2.smartlib.cn/interface.cx?";
    //public static String DEFAULT_URL = "http://114.212.7.87/book_center/interface.cx?";

    /**
     * 接口参数名称
     */
    public static final String PAR_CONTROL = "control";

    public static final String PAR_MAC_ADDRESS = "mac";

    public static final String PAR_LAYOUT_ID = "layout_id";

    public static final String PAR_MODEL_ID = "model_id";

    public static final String PAR_UPDATE_TAG = "update_tag";

    public static final String PAR_DEVICE_CODE = "bh";

    public static final String PAR_SCHOOL_BH = "school_bh";

    public static final String PAR_ID = "id";

    public static final String PAR_MODULE_ID = "module_id";

    public static final String PAR_MODULE_NAME = "module_name";

    public static final String PAR_PX = "px";

    public static final String PAR_KEYWORD = "keyword";

    public static final String PAR_SUBJECT = "subject";

    public static final String PAR_WIDGETS_ID = "widgets_id";

    public static final String PAR_SOURCE_TYPE = "source_type";

    public static final String PAR_TITLE = "title";

    public static final String PAR_CONTROL_TYPE = "control_type";

    public static final String PAR_CONTROL_INDEX = "control_index";

    public static final String PAR_CONTROL_HEIGHT = "control_height";

    public static final String PAR_CONTROL_NAME = "control_name";

    public static final String PAR_PAGE_SIZE = "pagesize";

    public static final String PAR_PAGE = "page";

    public static final String PAR_BH = "bh";

    public static final String PAR_CONTROL_ID = "control_id";

    public static final String PAR_KEY = "key";

    public static final String PAR_STATE = "state";

    public static final String PAR_LAST_CONNECT_TIME = "last_connect_time";

    public static final String PAR_VERSION = "version";

    public static final String PAR_VERSION_CODE = "version_code";

    public static final String PAR_VALUE = "value";

    public static final String PAR_PRE_SCENE = "pre_scene";

    public static final String PAR_SCENE_ID = "scene_id";

    public static final String PAR_PRE_MODULE = "pre_module";

    public static final String PAR_DEVICE_ID = "device_id";

    public static final String PAR_VERSION_ID = "version_id";

    public static final String PAR_TYPE = "type";

    public static final String PAR_CONFIG_GROUP_ID = "config_group_id";

    public static final String PAR_USERNAME = "username";

    public static final String PAR_PASSWORD = "password";

    /**
     * Control参数值
     */
    public static final String CONTROL_GET_SCHOOL_VERSION = "get_school_mobile_version";
    public static final String CONTROL_GET_VERSION_MODULE = "get_version_module";
    public static final String CONTROL_GET_SCHOOL_VERSION_BY_BH = "get_school_version_by_bh";
    public static final String CONTROL_GET_ALL_VERSION_MODULE = "get_all_version_module";
    public static final String CONTROL_GET_CONFIGS = "get_configs";

    /**
     * 豆瓣API
     */
    public static final String DOUBAN_URL_BOOK_BY_ISBN = "https://api.douban.com/v2/book/isbn/%s?apikey=022b1e3243fbff4a06815a96cbf3fdde";
    public static final String DOUBAN_URL_BOOK_BY_TAG = "https://api.douban.com/v2/book/search?%s&apikey=022b1e3243fbff4a06815a96cbf3fdde";
    public static final String DOUBAN_URL_BOOK_REVIEWS_BY_ISBN = "https://api.douban.com/v2/book/isbn/%s/reviews?apikey=022b1e3243fbff4a06815a96cbf3fdde";
    public static final String DOUBAN_URL_BOOK_TAGS_BY_ISBN = "https://api.douban.com/v2/book/isbn/%s/tags?apikey=022b1e3243fbff4a06815a96cbf3fdde";
    public static final String DOUBAN_EXCEPTION_BOOK_NOT_FOUND = "book_not_found";
    public static final String DOUBAN_EXCEPTION_REVIEW_NOT_FOUND = "review_not_found";
    public static final String DOUBAN_IMAGE = "Douban";

}
