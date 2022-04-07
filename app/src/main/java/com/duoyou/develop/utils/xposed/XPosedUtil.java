package com.duoyou.develop.utils.xposed;

import android.annotation.SuppressLint;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class XPosedUtil {

    @SuppressLint("SdCardPath")
    private static final String BASE_DIR_LEGACY = "/data/data/de.robv.android.xposed.installer/";

    public static final String BASE_DIR = Build.VERSION.SDK_INT >= 24
            ? "/data/user_de/0/de.robv.android.xposed.installer/" : BASE_DIR_LEGACY;

    public static final String ENABLED_MODULES_LIST_FILE = BASE_DIR + "conf/enabled_modules.list";

    private static final String[] XPOSED_PROP_FILES = new String[]{
            "/su/xposed/xposed.prop", // official systemless
            "/system/xposed.prop",    // classical
    };

    private static InstallZipUtil.XposedProp mXposedProp;

    public static void reloadXposedProp() {
        InstallZipUtil.XposedProp prop = null;
        if (prop == null) {
            try {
                for (String path : XPOSED_PROP_FILES) {
                    File file = new File(path);
                    if (file.canRead()) {
                        FileInputStream is = null;
                        try {
                            is = new FileInputStream(file);
                            prop = InstallZipUtil.parseXposedProp(is);
                            break;
                        } catch (Exception e) {

                        } finally {
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mXposedProp = prop;
    }

    public static InstallZipUtil.XposedProp getmXposedProp() {
        return mXposedProp;
    }

    public static int getXposedVersion() {
        if (mXposedProp != null) {
            return mXposedProp.getVersionInt();
        }
        return -1;
    }

}
