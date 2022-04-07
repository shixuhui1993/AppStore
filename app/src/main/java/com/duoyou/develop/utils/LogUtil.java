package com.duoyou.develop.utils;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;

public class LogUtil {
    public static final String TAG = " duo_log:";

    public static void i(String tag, String content) {
        if (AppInfoUtils.isDebug()) {
            if (StringUtils.isEmpty(content)) {
                Log.i(tag, "message = null");
                return;
            }
            Log.i(tag, content);
        }
    }
    public static void i( String content) {
        if (AppInfoUtils.isDebug()) {
            if (StringUtils.isEmpty(content)) {
                Log.i(TAG, "message = null");
                return;
            }
            Log.i(TAG, content);
        }
    }
    public static void e(String tag, String content) {
        if (AppInfoUtils.isDebug()) {
            Log.e(tag, content);
        }
    }
}
