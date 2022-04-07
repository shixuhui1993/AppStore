package com.duoyou.develop.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.Utils;

public class SPLoginManager {

    public static String PREFERENCES_NAME = "box_login_config";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String UID = "uid";
    public static final String MOBILE = "mobile";
    public static final String NICKNAME = "nickname";
    public static final String SIGN = "sign";
    public static final String AVATAR = "avatar";
    public static final String USERINFO_JSON = "user_info_json";
    public static final String USER_CHANNEL = "user_channel";
    public static final String USER_JSON = "user_json";
    public static final String USER_PACKET_GET = "user_packet_get";
    /**
     * 保存值
     */
    public static void putValue(String key, String value) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
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
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 获取值
     */
    public static int getValue(String key) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 保存值
     */
    public static void putValue(String key, boolean value) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
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
        sp.edit().clear().commit();
    }


}
