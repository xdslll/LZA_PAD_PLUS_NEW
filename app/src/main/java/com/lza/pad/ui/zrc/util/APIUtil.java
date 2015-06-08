package com.lza.pad.ui.zrc.util;

import android.os.Build;

/**
 *
 * Created by lansing on 2015/6/3.
 */
public class APIUtil {
    public static boolean isSupport(int apiNo) {
        return Build.VERSION.SDK_INT >= apiNo;
    }
}
