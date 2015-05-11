package lza.com.lza.library.exception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import lza.com.lza.library.util.ToastUtils;

/**
 * 捕获全局异常事件
 *
 * @author xiads
 * @Date 10/27/14.
 */
public class CrashHelper implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHelper mCrashHelper;
    private Context mCtx;
    private CrashListener mCrashListener = null;

    private CrashHelper(Context context, CrashListener listener) {
        this.mCtx = context;
        if (listener != null) {
            this.mCrashListener = listener;
        }
    }

    private CrashHelper(Context context, Class<?> clazz) {
        this.mCtx = context;
        this.mCrashListener = new DefaultCrashListner(clazz);
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static CrashHelper getInstance(Context context, CrashListener listener) {
        if (mCrashHelper == null) {
            mCrashHelper = new CrashHelper(context, listener);
        }
        return mCrashHelper;
    }

    public static CrashHelper getInstance(Context context, Class<?> clazz) {
        if (mCrashHelper == null) {
            mCrashHelper = new CrashHelper(context, clazz);
        }
        return mCrashHelper;
    }

    public interface CrashListener {
        void onCrash(Thread thread, Throwable ex);
    }

    private class DefaultCrashListner implements CrashListener {

        private Class clazz;

        public DefaultCrashListner(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public void onCrash(Thread thread, Throwable ex) {
            restart(mCtx, clazz);
        }
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        ex.printStackTrace();
        if(!handleException(thread, ex) && mDefaultHandler != null){
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            if (mCrashHelper != null) {
                mCrashListener.onCrash(thread, ex);
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Thread thread, final Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                ToastUtils.showLong(mCtx, "很抱歉程序出现异常，即将为您重启程序。");
                Looper.loop();
            }
        }.start();
        return true;
    }

    /**
     * 重启应用
     *
     * @param c
     * @param clazz
     */
    public void restart(Context c, Class<?> clazz) {
        //等待1.5秒重新启动程序
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setClass(c, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        PendingIntent restartIntent = PendingIntent.getActivity(
                c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //退出程序
        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); //1秒钟后重启应用
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
