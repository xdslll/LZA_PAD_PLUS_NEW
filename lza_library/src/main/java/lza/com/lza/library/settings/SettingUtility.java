package lza.com.lza.library.settings;

import android.content.Context;

/**
 * User: qii
 * Date: 12-11-28
 */
public class SettingUtility {

    private Context mCtx;
    private static SettingUtility mInstance = null;

    private static final String FIRSTSTART = "firststart";

    private SettingUtility(Context c) {
        mCtx = c;
    }

    public static SettingUtility getInstance(Context c) {
        if (mInstance == null) {
            mInstance = new SettingUtility(c);
        }
        return mInstance;
    }

    public boolean firstStart() {
        boolean value = SettingHelper.getSharedPreferences(mCtx, FIRSTSTART, true);
        if (value) {
            SettingHelper.setEditor(mCtx, FIRSTSTART, false);
        }
        return value;
    }
}
