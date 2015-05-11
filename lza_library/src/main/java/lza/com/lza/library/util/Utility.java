package lza.com.lza.library.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiads on 14-9-7.
 */
public class Utility {

    /**
     * 关闭IO
     *
     * @param io
     */
    public static void close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印数组
     *
     * @param arr
     */
    public static void printArray(String[] arr) {
        for (String str : arr) {
            AppLogger.e("str-->" + str);
        }
    }

    /**
     * 遍历Cursor中的信息，并打印字段名和字段值到Logcat中
     *
     * @param cursor
     */
    public static void printCursor(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int colCount = cursor.getColumnCount();
                    for (int i = 0; i < colCount; i++) {
                        String colName = cursor.getColumnName(i);
                        AppLogger.e(colName + "-->" + cursor.getString(i));
                    }
                } while (cursor.moveToNext());
                cursor.moveToFirst();
            }
        }
    }

    /**
     * 遍历文件夹，打印所有的文件和文件夹
     *
     * @param dir
     */
    public static void printDir(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            printFiles(dir.listFiles());
        }
    }

    /**
     * 打印文件数组
     *
     * @param files
     */
    public static void printFiles(File[] files) {
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    printFiles(f.listFiles());
                }else if (f.isFile()) {
                    AppLogger.d("[" + f.getName() + "]-->[" + f.getAbsolutePath() + "]");
                }else {
                    continue;
                }
            }
        }
    }

    /**
     * URL编码
     *
     * @param s
     * @return
     */
    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {
                    params.putString(URLDecoder.decode(v[0], "UTF-8"),
                            URLDecoder.decode(v[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return params;
    }

    /**
     * URL解码
     *
     * @param param
     * @return
     */
    public static String encodeUrl(Map<String, String> param) {
        if (param == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Set<String> keys = param.keySet();
        boolean first = true;

        for (String key : keys) {
            String value = param.get(key);
            //pain...EditMyProfileDao params' values can be empty
            if (value != null || key.equals("description") || key.equals("url")) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }
                try {
                    sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                            .append(URLEncoder.encode(param.get(key), "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }
            }
        }

        return sb.toString();
    }

    /**
     * 判断map是否为空
     *
     * @param map
     * @return
     */
    public static <K, V> boolean isMapEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断当前的时间是白天还是黑夜
     *
     * @return
     */
    public static boolean isDayOrNight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 18)
            return true;
        else
            return false;
    }

    /**
     * 获取屏幕尺寸
     *
     * @param activity
     * @return
     */
    public static Point getPoint(Activity activity) {
        //获取屏幕分辨率
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telManager != null) {
            return telManager.getDeviceId();
        } else {
            return "";
        }
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static String getNetworkTypeName(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
        if (networkinfo != null) {
            return networkinfo.getTypeName();
        } else {
            return "";
        }
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            return info.getMacAddress();
        } else {
            return "";
        }
    }

    /**
     * 设置View是否可见,0-不可见,1-可见
     *
     * @param view
     * @param isViewVisble
     */
    public static void setViewVisibility(View view, int isViewVisble) {
        if (isViewVisble == 1)
            setViewVisbility(view, true);
        else
            setViewVisbility(view, false);
    }

    /**
     * 设置View是否可见,true-可见,false-不可见
     *
     * @param view
     * @param isVisble
     */
    public static void setViewVisbility(View view, boolean isVisble) {
        if (view != null) {
            if (isVisble)
                view.setVisibility(View.VISIBLE);
            else
                view.setVisibility(View.GONE);
        }
    }

    public static AlertDialog createDialog(Context context, int title, int message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", cancelListener)
                .create();
    }

    public static AlertDialog createDialog(Context context, int title, int message, DialogInterface.OnClickListener okListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .create();
    }

    public static AlertDialog createDialog(Context context, String title, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", cancelListener)
                .create();
    }

    public static AlertDialog createDialog(Context context, String title, String message, DialogInterface.OnClickListener okListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .create();
    }

    public static AlertDialog createDialog(Context context, String title, String message, String okButtonLabel, String cancelButtonLabel, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(okButtonLabel, okListener)
                .setNegativeButton(cancelButtonLabel, cancelListener)
                .create();
    }

    public static AlertDialog createDialog(Context context, int title, int message, int okButtonLabel, int cancelButtonLabel, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(okButtonLabel, okListener)
                .setNegativeButton(cancelButtonLabel, cancelListener)
                .create();
    }

    public static void showDialog(Context context, int title, int message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        createDialog(context, title, message, okListener, cancelListener).show();
    }

    public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        createDialog(context, title, message, okListener, cancelListener).show();
    }

    public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener okListener) {
        createDialog(context, title, message, okListener).show();
    }

    public static void showDialog(Context context, int title, int message, DialogInterface.OnClickListener okListener) {
        createDialog(context, title, message, okListener).show();
    }

    public static void showDialog(Context context, int title, int message) {
        createDialog(context, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public static void showDialog(Context context, String title, String message) {
        createDialog(context, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public static void showDialog(Context context, int title, int message, int okButtonLabel, int cancelButtonLabel, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        createDialog(context, title, message, okButtonLabel, cancelButtonLabel, okListener, cancelListener).show();
    }

    public static int getIntHalfUp(float f) {
        BigDecimal bd = new BigDecimal(f).setScale(BigDecimal.ROUND_HALF_UP);
        return bd.intValue();
    }

    public static float getFloatByHalfUp(float f) {
        BigDecimal bd = new BigDecimal(f).setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    /**
     * 获得超类的参数类型，取第一个参数类型
     * @param <T> 类型参数
     * @param clazz 超类类型
     */
    @SuppressWarnings("rawtypes")
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 根据索引获得超类的参数类型
     * @param clazz 超类类型
     * @param index 索引
     */
    @SuppressWarnings("rawtypes")
    public static Class getClassGenricType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    public static String wrap(String value, String defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : value;
    }

    public static int safeIntParse(String digitValue, int defaultValue) {
        try {
            return Integer.parseInt(digitValue);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 获取处于栈顶的Activity
     *
     * @return
     */
    public static String getTopActivity(Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        if (runningTasks == null || runningTasks.size() == 0) return null;
        String className = runningTasks.get(0).topActivity.getShortClassName();
        if (TextUtils.isEmpty(className) || !className.contains(".")) return null;
        int index = className.lastIndexOf(".");
        return className.substring(index + 1, className.length());
    }

    public static boolean isTopActivity(Activity activity) {
        String topActivity = getTopActivity(activity);
        String currentActivity = activity.getClass().getSimpleName();
        return topActivity.equals(currentActivity);
    }

    /**
     * 用字符串生成二维码
     *
     * @param str
     * @author zhouzhe@lenovo-cw.com
     * @return
     * @throws WriterException
     */
    public static Bitmap createQCode(String str, int mapWidth, int mapHeight) {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败

        String utf8Str = str;
        try {
            utf8Str = new String(str.getBytes("utf-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(utf8Str,
                    BarcodeFormat.QR_CODE, mapWidth, mapHeight);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            // 二维矩阵转为一维像素数组,也就是一直横着排了
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            // 通过像素数组生成bitmap,具体参考api
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 强制刷新系统相册
     *
     * @param c
     * @param path
     */
    public static void forceRefreshSystemAlbum(Context c, String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        String type = options.outMimeType;

        MediaScannerConnection
                .scanFile(c, new String[]{path}, new String[]{type},
                        null);
    }

    public static int dip2px(Context context, int dipValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    public static int px2dip(Context context, int pxValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    public static float sp2px(Context context, int spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                context.getResources().getDisplayMetrics());
    }

    public static int length(String paramString) {
        int i = 0;
        for (int j = 0; j < paramString.length(); j++) {
            if (paramString.substring(j, j + 1).matches("[Α-￥]")) {
                i += 2;
            } else {
                i++;
            }
        }

        if (i % 2 > 0) {
            i = 1 + i / 2;
        } else {
            i = i / 2;
        }

        return i;
    }

    private static DisplayMetrics metrics = null;
    public static int getScreenWidth(Activity activity) {
        if (metrics != null) return metrics.widthPixels;
        else metrics = new DisplayMetrics();
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }

        return 0;
    }

    public static int getScreenHeight(Activity activity) {
        if (metrics != null) return metrics.heightPixels;
        else metrics = new DisplayMetrics();
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            display.getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 0;
    }

    public static boolean root() {
        boolean hasRoot = false;
        try {
            //获取Root权限
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                hasRoot = true;
            }
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return hasRoot;
    }

    public static void execShellCmd(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
