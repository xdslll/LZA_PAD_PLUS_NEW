package com.lza.pad.helper;

import android.content.Context;

import lza.com.lza.library.settings.SettingHelper;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/11/15.
 */
public class Settings implements Consts.ParamKey {

    public static void setSchoolBh(Context c, String bh) {
        SettingHelper.setEditor(c, KEY_SCHOOL_BH, bh);
    }

    public static String getSchoolBh(Context c) {
        return SettingHelper.getSharedPreferences(c, KEY_SCHOOL_BH, "");
    }

    public static boolean removeSchoolBh(Context c) {
        return SettingHelper.remove(c, KEY_SCHOOL_BH);
    }

    public static void setSchoolName(Context c, String name) {
        SettingHelper.setEditor(c, KEY_SCHOOL_NAME, name);
    }

    public static String getSchoolName(Context c) {
        return SettingHelper.getSharedPreferences(c, KEY_SCHOOL_NAME, "");
    }

    public static boolean removeSchoolName(Context c) {
        return SettingHelper.remove(c, KEY_SCHOOL_NAME);
    }

    public static void setIfSkipLogin(Context c, boolean ifSkip) {
        SettingHelper.setEditor(c, KEY_IF_SKIP_LOGIN, ifSkip);
    }

    public static boolean getIfSkipLogin(Context c) {
        return SettingHelper.getSharedPreferences(c, KEY_IF_SKIP_LOGIN, false);
    }
}
