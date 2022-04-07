package com.duoyou.develop.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashSet;

public class DeviceIdUtils {

    public static String getDeviceId(Context context, int slotId) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = telephonyManager.getClass().getMethod("getDeviceId", int.class);
            return (String) method.invoke(telephonyManager, slotId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getImei(Context context, int slotId) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = telephonyManager.getClass().getMethod("getImei", int.class);
            return (String) method.invoke(telephonyManager, slotId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getImei(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = telephonyManager.getClass().getMethod("getImei");
            return (String) method.invoke(telephonyManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONObject getDeviceIds(Context context) {
        try {
            String imei1 = getDeviceId(context, 0);
            String imei2 = getDeviceId(context, 1);
            String imei3 = getImei(context, 0);
            String imei4 = getImei(context, 1);
            String imei5 = getDeviceId(context);
            String imei6 = getImei(context);
            HashSet<String> hashSet = new HashSet<>();
            if (!TextUtils.isEmpty(imei1)) {
                hashSet.add(imei1);
            }
            if (!TextUtils.isEmpty(imei2)) {
                hashSet.add(imei2);
            }
            if (!TextUtils.isEmpty(imei3)) {
                hashSet.add(imei3);
            }
            if (!TextUtils.isEmpty(imei4)) {
                hashSet.add(imei4);
            }
            if (!TextUtils.isEmpty(imei5)) {
                hashSet.add(imei5);
            }
            if (!TextUtils.isEmpty(imei6)) {
                hashSet.add(imei6);
            }
            JSONObject jsonObject = new JSONObject();
            int i = 0;
            for (String value : hashSet) {
                i++;
                jsonObject.put(String.valueOf(i), value);
            }

            String dyid = getDyIdMd5(context);
//            LogUtil.i("dyid__", dyid);
            if (!TextUtils.isEmpty(dyid)) {
                jsonObject.put("5", dyid);
            }
//
            String androidId = getAndroidId(context);
            if (!TextUtils.isEmpty(androidId)) {
                jsonObject.put("6", androidId);
            }
            if (Build.VERSION.SDK_INT >= 29) {
                String value = SPManager.getValue(SPManager.OAID, "");
                if (!TextUtils.isEmpty(value)) {
                    jsonObject.put("7", value);
                }
            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static String getAndroidId(Context context) {
        try {
            return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDyIdMd5(Context context) {
        String utdid = LocalStorageUtils.getUtdid(context);
        // utdid 生成MD5 16 位的。
        String utdidMd5 = SignUtil.MD5_16(utdid);
        if (!TextUtils.isEmpty(utdidMd5) && utdidMd5.length() > 9) {
            String leftString = utdidMd5.substring(0, 9);
            String middleString = "1";
            String rightString = utdidMd5.substring(9);
            utdidMd5 = leftString + middleString + rightString;
        }
        return utdidMd5;
    }

}
