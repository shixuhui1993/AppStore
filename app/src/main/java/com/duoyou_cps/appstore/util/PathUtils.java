package com.duoyou_cps.appstore.util;

import android.app.Application;
import android.os.Environment;

import com.blankj.utilcode.util.StringUtils;

import java.io.File;

public class PathUtils {

    private static String DOWNLOAD_PATH;
    private static String DOWNLOAD_FILE_PATH;
    private static String DOWNLOAD_CACHE_PATH;
    private static String UPLOAD_PIC_PATH;
    private static String USER_FILE_PATH;

    public static String SYSTEM_DOWNLOAD_FILEPATH;

    public static void initPathConfig(Application application) {
        String externalFilesDir = application.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath();
        if (!StringUtils.isEmpty(externalFilesDir)) {
            String path = Environment.getExternalStorageDirectory().getPath();
            DOWNLOAD_FILE_PATH = externalFilesDir+"/";
            USER_FILE_PATH = externalFilesDir+"/user_account/";
//            DOWNLOAD_PATH = path + "/" + BuildConfig.APPLICATION_ID + "/download/";
//            DOWNLOAD_FILE_PATH = path + "/" + BuildConfig.APPLICATION_ID + "/download/files/";
//            DOWNLOAD_CACHE_PATH = path + "/" + BuildConfig.APPLICATION_ID + "/download/cache/";
//            UPLOAD_PIC_PATH = path + "/" + BuildConfig.APPLICATION_ID + "/download/upload/";
        } else {
            String path = application.getApplicationInfo().dataDir;
            DOWNLOAD_PATH = path+"/download/";
            DOWNLOAD_FILE_PATH = path + "/download/files/";
            DOWNLOAD_CACHE_PATH = path + "/download/cache/";
            UPLOAD_PIC_PATH = path + "/download/upload/";
        }
    }


    public static String getUserFilePath() {
        mkDirs(USER_FILE_PATH);
        return DOWNLOAD_PATH;
    }
 public static String getDownloadPath() {
        mkDirs(DOWNLOAD_PATH);
        return DOWNLOAD_PATH;
    }

    public static String getDownloadCachePath() {
        mkDirs(DOWNLOAD_CACHE_PATH);
        return DOWNLOAD_CACHE_PATH;
    }
    public static String getSavePicPath(){
        mkDirs(UPLOAD_PIC_PATH);
        return UPLOAD_PIC_PATH;
    }

    public static String getDownloadFilePath() {
        mkDirs(DOWNLOAD_FILE_PATH);
        return DOWNLOAD_FILE_PATH;
    }

    public static void mkDirs(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                boolean mkdirs1 = mkdirs;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
