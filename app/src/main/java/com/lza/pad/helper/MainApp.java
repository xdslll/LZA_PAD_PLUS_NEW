package com.lza.pad.helper;

import android.app.Application;

import lza.com.lza.library.exception.CrashManager;
import lza.com.lza.library.util.AppLogger;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/7/15.
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashManager.registerHandler();
        AppLogger.setIfAllowDebug(true);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
