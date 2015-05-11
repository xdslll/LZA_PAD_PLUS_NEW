package lza.com.lza.library.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lza.com.lza.library.util.AppLogger;
import lza.com.lza.library.util.Utility;

/**
 * User: qii
 * Date: 12-8-3
 */
public class FileManager {

    private static final String PICTURE_CACHE = "lza/picture_cache";
    private static final String TXT2PIC = "lza/txt2pic";
    private static final String WEBVIEW_FAVICON = "lza/favicon";
    private static final String LOG = "lza/log";
    private static final String LZA = "lza";

    public static String getSdCardPath(Context c) {
        if (isExternalStorageMounted()) {
            File path = c.getExternalCacheDir();
            if (path != null) {
                return path.getAbsolutePath();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public File getAlbumStorageDir(String albumName) {

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            AppLogger.e("Directory not created");
        }
        return file;
    }

    public static boolean isExternalStorageMounted() {

        boolean canRead = Environment.getExternalStorageDirectory().canRead();
        boolean onlyRead = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean unMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_UNMOUNTED);

        return !(!canRead || onlyRead || unMounted);
    }

    public static String getLogDir(Context c) {
        if (!isExternalStorageMounted()) {
            return "";
        } else {
            String path = getSdCardPath(c) + File.separator + LOG;
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            return path;
        }
    }

    public static String generateDownloadFileName(Context c, String url) {

        if (!isExternalStorageMounted()) {
            return "";
        }

        if (TextUtils.isEmpty(url)) {
            return "";
        }

        String path = String.valueOf(url.hashCode());
        String result = getSdCardPath(c) + File.separator + PICTURE_CACHE + File.separator + path;
        if (url.endsWith(".jpg")) {
            result += ".jpg";
        } else if (url.endsWith(".gif")) {
            result += ".gif";
        }
        if (!result.endsWith(".jpg") && !result.endsWith(".gif") && !result.endsWith(".png")) {
            result = result + ".jpg";
        }

        return result;
    }

    public static String getTxt2picPath(Context c) {
        if (!isExternalStorageMounted()) {
            return "";
        }

        String path = getSdCardPath(c) + File.separator + TXT2PIC;
        File file = new File(path);
        if (file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static File createNewFileInSDCard(String absolutePath) {
        if (!isExternalStorageMounted()) {
            AppLogger.e("sdcard unavailiable");
            return null;
        }

        if (TextUtils.isEmpty(absolutePath)) {
            return null;
        }

        File file = new File(absolutePath);
        if (file.exists()) {
            return file;
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                AppLogger.d(e.getMessage());
                return null;
            }
        }
        return null;
    }

    public static String getWebViewFaviconDirPath(Context c) {
        if (!TextUtils.isEmpty(getSdCardPath(c))) {
            String path = getSdCardPath(c) + File.separator + WEBVIEW_FAVICON + File.separator;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return path;
        }
        return "";
    }

    public static String getCacheSize(Context c) {
        if (isExternalStorageMounted()) {
            String path = getSdCardPath(c) + File.separator;
            FileSize size = new FileSize(new File(path));
            return size.toString();
        }
        return "0MB";
    }

    public static List<String> getCachePath(Context c) {
        List<String> path = new ArrayList<String>();
        if (isExternalStorageMounted()) {
            String thumbnailPath = getSdCardPath(c) + File.separator + PICTURE_CACHE;

            path.add(thumbnailPath);
        }
        return path;
    }

    public static String getPictureCacheSize(Context c) {
        long size = 0L;
        if (isExternalStorageMounted()) {
            String thumbnailPath = getSdCardPath(c) + File.separator + PICTURE_CACHE;

            size += new FileSize(new File(thumbnailPath)).getLongSize();
        }
        return FileSize.convertSizeToString(size);
    }

    public static boolean deleteCache(Context c) {
        String path = getSdCardPath(c) + File.separator;
        return deleteDirectory(new File(path));
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static boolean saveToPicDir(Context c, String path) {
        if (!isExternalStorageMounted()) {
            return false;
        }

        File file = new File(path);
        String name = file.getName();
        String newPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + LZA
                + File.separator + name;
        try {
            FileManager.createNewFileInSDCard(newPath);
            copyFile(file, new File(newPath));
            Utility.forceRefreshSystemAlbum(c, newPath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }
}
