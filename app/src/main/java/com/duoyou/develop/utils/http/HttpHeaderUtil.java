package com.duoyou.develop.utils.http;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.Base64;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.duoyou.develop.utils.AppInfoUtils;
import com.duoyou.develop.utils.CommonUtils;
import com.duoyou.develop.utils.DeviceIdUtils;
import com.duoyou.develop.utils.DeviceInfoUtils;
import com.duoyou.develop.utils.LocalStorageUtils;
import com.duoyou.develop.utils.NetworkUtils;
import com.duoyou.develop.utils.SPManager;
import com.duoyou.develop.utils.SignUtil;
import com.duoyou.develop.utils.xposed.XPosedUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaderUtil {

    public static Map<String, String> map = new ArrayMap<>();

    public static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static Map<String, String> getHeaderInfoMap() {
        Context context = AppInfoUtils.getApplication();
        try {
//            String token = UserInfo.getInstance().getAccessToken();
//            map.put("token", token);
//            map.put("user_id", UserInfo.getInstance().getUid() + "");
//            map.put("user_name", UserInfo.getInstance().getNickname());
//            map.put("risk_control", CheckActivity.riskControl + "");
//            if (isEmpty(map.get("channel"))) {
            map.put("channel", AppInfoUtils.getChannel());
//            }
            String udtid = LocalStorageUtils.getUtdid(context);
            if (isEmpty(map.get("devicekey"))) {
                map.put("devicekey", udtid);
            }
            if (isEmpty(map.get("device_type"))) {
                map.put("device_type", "1");
            }

            if (isEmpty(map.get("mac"))) {
                map.put("mac", DeviceInfoUtils.getMacAddress(context));
            }
            if (isEmpty(map.get("duo_you_sdk_channel"))) {
                map.put("duo_you_sdk_channel", "");
            }
            if (isEmpty(map.get("network"))) {
                map.put("network", NetworkUtils.getNetworkState(context) + "");
            }
            if (isEmpty(map.get("phonename"))) {
                map.put("phonename", "");
            }
            if (isEmpty(map.get("sim"))) {
                int hasSim = CommonUtils.hasSimCard();
                map.put("sim", hasSim + "");
            }

            if (isEmpty(map.get("utdid"))) {
                map.put("utdid", udtid);
            }

            if (isEmpty(map.get("resolution"))) {
                int width = ScreenUtils.getScreenWidth();
                int height = ScreenUtils.getScreenHeight();
                map.put("resolution", width + "*" + height);
            }

            if (isEmpty(map.get("boot_time"))) {
                String bootTime = SystemClock.elapsedRealtime() + "";
                map.put("boot_time", bootTime);
            }


            if (isEmpty(map.get("installtime"))) {
                map.put("installtime", CommonUtils.getAppInstallTime() + "");
            }

            String value = SPManager.getValue(SPManager.OAID, "");
            map.put("oaid", value);

            if (isEmpty(map.get("system_version"))) {
                map.put("system_version", Build.VERSION.RELEASE); // 系统版本
            }

            if (isEmpty(map.get("system_model"))) {
                map.put("system_model", Build.MODEL); // 系统厂商
            }

            if (isEmpty(map.get("os"))) {
                map.put("os", Build.BRAND); // 设备型号
            }

//            if (isEmpty(map.get("packagename"))) {
//                map.put("packagename", BuildConfig.APPLICATION_ID);
//            }
//            if (isEmpty(map.get("package_name"))) {
//                map.put("package_name", BuildConfig.APPLICATION_ID);
//            }

            if (isEmpty(map.get("versioncode"))) {
                map.put("versioncode", AppInfoUtils.getVerCode() + "");
            }
            if (isEmpty(map.get("versionname"))) {
                map.put("versionname", AppInfoUtils.getVerCodeStr());
            }

            if (isEmpty(map.get("device_num"))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (AppInfoUtils.getApplication().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        map.put("device_num", PhoneUtils.getIMEI());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            map.put("device_ids", DeviceIdUtils.getDeviceIds(context).toString());
            map.put("xposed", XPosedUtil.getXposedVersion() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = new Gson().toJson(map);
        String sign = SignUtil.getSignWithMap(map);
        map.put("sign", sign);

        return map;
    }

    public static String getHeaderInfoJsonString() {
        try {
            Context context = AppInfoUtils.getApplication();
            Map<String, String> map = new HashMap<>(getHeaderInfoMap());
            // 以为js解析不出来带转义字符的json，所以要移除
            map.remove("device_ids");
            JSONObject jsonObject = new JSONObject(map);
            jsonObject.put("device_ids", DeviceIdUtils.getDeviceIds(context));
            jsonObject.put("channel", AppInfoUtils.getChannel());
            jsonObject.put("versioncode", AppInfoUtils.getVerCode());
            String headInfoJson = jsonObject.toString();
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getHeaderInfoJsonStringBase64() {
        try {
            return Base64.encodeToString(HttpHeaderUtil.getHeaderInfoJsonString().getBytes(), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void clear() {
        if (map != null) {
            map.clear();
        }
    }

}
