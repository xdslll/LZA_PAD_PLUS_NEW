package lza.com.lza.library.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * User: qii
 * Date: 12-11-28
 */
public class SettingHelper {
    private static SharedPreferences.Editor editor = null;
    private static SharedPreferences sharedPreferences = null;

    private SettingHelper() {

    }

    private static SharedPreferences.Editor getEditorObject(Context paramContext) {
        if (editor == null) {
            editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
        }
        return editor;
    }

    public static int getSharedPreferences(Context paramContext, String key, int value) {
        return getSharedPreferencesObject(paramContext).getInt(key, value);
    }

    public static long getSharedPreferences(Context paramContext, String key,
            long value) {
        return getSharedPreferencesObject(paramContext).getLong(key, value);
    }

    public static Boolean getSharedPreferences(Context paramContext, String key,
            Boolean value) {
        return getSharedPreferencesObject(paramContext).getBoolean(key, value);
    }

    public static String getSharedPreferences(Context paramContext, String key,
            String value) {
        return getSharedPreferencesObject(paramContext).getString(key, value);
    }

    private static SharedPreferences getSharedPreferencesObject(Context paramContext) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
        }
        return sharedPreferences;
    }

    public static boolean setEditor(Context paramContext, String key, int value) {
        return getEditorObject(paramContext).putInt(key, value).commit();
    }

    public static boolean setEditor(Context paramContext, String key, long value) {
        return getEditorObject(paramContext).putLong(key, value).commit();
    }

    public static boolean setEditor(Context paramContext, String key, Boolean value) {
        return getEditorObject(paramContext).putBoolean(key, value).commit();
    }

    public static boolean setEditor(Context paramContext, String key, String value) {
        return getEditorObject(paramContext).putString(key, value).commit();
    }

    public static boolean remove(Context c, String key) {
        return getEditorObject(c).remove(key).commit();
    }
}
