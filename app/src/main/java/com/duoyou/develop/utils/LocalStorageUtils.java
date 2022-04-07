package com.duoyou.develop.utils;


import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;


public class LocalStorageUtils {

    private static boolean saveUtdid(Context context, String accessToken) {
        String path = getFilePath();
        return FileUtil.writeString(path, accessToken, "UTF-8");
    }

//    public static String getUtdid(Context context) {
//        String path = getFilePath();
//        String utdid = SPManager.getValue(context, SPManager.UTDID, "");
//        if (TextUtils.isEmpty(utdid)) {
//            String utdidEncrpty = FileUtil.readString(path, "UTF-8");
//            String utdidDesctpty = EasyAES.decryptString(utdidEncrpty);
//            if (TextUtils.isEmpty(utdidDesctpty)) {
//                String oaid = SPManager.getValue(context, SPManager.OAID, "");
//                utdid = UTDevice.getUtdid(context);
//                utdidEncrpty = EasyAES.encryptString(utdid);
//                boolean writeSuccess = saveUtdid(context, utdidEncrpty);
//                if (Build.VERSION.SDK_INT >= 29 && !TextUtils.isEmpty(oaid) && !writeSuccess) {
//                    utdid = SignUtil.MD5(oaid).toLowerCase();
//                }
//                SPManager.putValue(context, SPManager.UTDID, utdid);
//            } else {
//                utdid = utdidDesctpty;
//                SPManager.putValue(context, SPManager.UTDID, utdid);
//            }
//        } else {
//            String utdidEncrpty = FileUtil.readString(path, "UTF-8");
//            String utdidDesctpty = EasyAES.decryptString(utdidEncrpty);
//            if (TextUtils.isEmpty(utdidDesctpty)) {
//                utdidEncrpty = EasyAES.encryptString(utdid);
//                saveUtdid(context, utdidEncrpty);
//            }
//        }
//
//        return utdid;
//    }

    public static String getUtdid(Context context) {
        String utdid = "";
        try {
            String path = getFilePath();
            utdid = SPManager.getValue( SPManager.UTDID, "");
            if (TextUtils.isEmpty(utdid)) {
                String utdidEncrpty = FileUtil.readString(path, "UTF-8");
                String utdidDesctpty = EasyAES.decryptString(utdidEncrpty);
                if (TextUtils.isEmpty(utdidDesctpty)) {
                    String androidId = Settings.System.getString(context.getContentResolver(),Settings.System.ANDROID_ID);
                    utdid = SignUtil.MD5(androidId);
                    utdidEncrpty = EasyAES.encryptString(utdid);
                    saveUtdid(context, utdidEncrpty);
                } else {
                    utdid = utdidDesctpty;
                }
                SPManager.putValue(SPManager.UTDID, utdid);
            } else {
                String utdidEncrpty = FileUtil.readString(path, "UTF-8");
                String utdidDesctpty = EasyAES.decryptString(utdidEncrpty);
                if (TextUtils.isEmpty(utdidDesctpty)) {
                    utdidEncrpty = EasyAES.encryptString(utdid);
                    saveUtdid(context, utdidEncrpty);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utdid;
    }

    private static String getFilePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.duoyou.sdk/";
        mkDirs(path);
        return path + "dy";
    }


    private static void mkDirs(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
