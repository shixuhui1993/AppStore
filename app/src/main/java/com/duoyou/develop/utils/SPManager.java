package com.duoyou.develop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.Utils;


public class SPManager {
    public static final String RECENT_ACCOUNT_STRING = "recent_account_String";
    public static String PREFERENCES_NAME = "box_config";
    public final static String AGREE_USER_PROTOCOL = "agree_user_protocol_" + AppInfoUtils.getVerCode();

    public static final String OAID = "oaid";
    public static final String UTDID = "utdid";
    public static final String AD_CODE_NEW_USER_SAVE_TIME = "ad_code_new_user_save_time";
    public static final String SERVER_RELEASE = "server_release";


    /**
     * 获取值
     */
    public static String getValue(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }
    /**
     * 保存值
     */
    public static void putValue(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }
    /**
     * 保存值
     */
    public static void putValue(String key, String value) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * 获取值
     */
    public static String getValue(String key, String defaultValue) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 保存值
     */
    public static void putValue(String key, int value) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 获取值
     */
    public static int getValue(String key, int defaultValue) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 保存值
     */
    public static void putValue(String key, boolean value) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value).apply();
    }

    /**
     * 获取值
     */
    public static boolean getValue(String key, boolean defaultValue) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void clear() {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static void clearAdSaveTime() {
        putValue(AD_CODE_NEW_USER_SAVE_TIME, "");
    }
}
