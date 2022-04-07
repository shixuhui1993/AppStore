//package com.duoyou.develop.utils.thinkingdata;
//
//import android.util.ArrayMap;
//import android.view.View;
//
//import com.blankj.utilcode.util.TimeUtils;
//import com.blankj.utilcode.util.Utils;
//import com.duoyou.develop.utils.AppInfoUtils;
//import com.duoyou.develop.entity.UserInfo;
//import com.duoyou.develop.utils.DeviceIdUtils;
//import com.duoyou.develop.utils.LogUtil;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import cn.thinkingdata.android.ThinkingAnalyticsSDK;
//
//public class ThinkingDataEvent {
//    private static ThinkingAnalyticsSDK eventSdk;
//
//    public static void initEventSdk(ThinkingAnalyticsSDK event) {
//        eventSdk = event;
//    }
//
//    public static final String ACTIVITY_OPEN = "user_register_step_event";
//    public static final String ACTIVITY_TAB_CHANGE = "main_tab_change";
//    public static final String STATISTICS_SHOW = "statistics_show";
//    public static final String STATISTICS_CLICK = "statistics_click";
//
//
//    public static void viewEvent(View view, String viewID) {
//        if (eventSdk == null) {
//            return;
//        }
//        eventSdk.setViewID(view, viewID);
//    }
//
//    public static void clickEventTrack(String eventId, Object... strings) {
//
//        ArrayMap<String, Object> arrayMap = new ArrayMap<>();
//
//        if (strings != null && strings.length % 2 != 1) {
//            if(strings.length == 0){
//                eventTrack(eventId);
//            }
//            String key = null;
//            Object value;
//            for (int i = 0; i < strings.length; i++) {
//                if (i % 2 == 0) {
//                    key = (String) strings[i];
//                    continue;
//                } else {
//                    value = strings[i];
//                }
//                if (key != null) {
//                    arrayMap.put(key, value);
//                    key = null;
//                }
//            }
//        }
//        eventTrack(eventId, arrayMap);
//    }
//
//    public static void eventTrack(String eventId) {
//        eventTrack(eventId, null);
//    }
//
//    public static void eventTrack(String eventId, ArrayMap<String, Object> params) {
//        if (eventSdk == null) {
//            return;
//        }
//        JSONObject properties;
//        try {
//            if (params != null) {
//                properties = new JSONObject(params);
//            } else {
//                properties = new JSONObject();
//            }
//            if (UserInfo.getInstance().isLogin()) {
//                properties.put("user_name", UserInfo.getInstance().getNickname());
//                properties.put("user_id", UserInfo.getInstance().getUid());
//            }
//            LogUtil.i("eventTrack ", properties.toString());
//            eventSdk.track(eventId, properties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void login() {
//        if (eventSdk == null) {
//            return;
//        }
//        // 记录登录事件
//        try {
//            JSONObject properties = new JSONObject();
//            properties.put("user_name", UserInfo.getInstance().getNickname());
//            properties.put("user_id", UserInfo.getInstance().getUid());
//            eventSdk.track("user_login", properties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 用户注册埋点
//     */
//    public static void userRegister() {
//        if (eventSdk == null) {
//            return;
//        }
//        // 记录注册事件
//        try {
//            JSONObject properties = new JSONObject();
//            String nowString = TimeUtils.getNowString();
//            properties.put("#first_check_id", "user_register");
//            properties.put("channel", AppInfoUtils.getChannel());
//            properties.put("first_login_time", nowString);
//            properties.put("user_register_time", nowString);
//            properties.put("last_login_time", nowString);
//            properties.put("user_name", UserInfo.getInstance().getNickname());
//            properties.put("user_id", UserInfo.getInstance().getUid());
//            properties.put("device_id", DeviceIdUtils.getDeviceId(Utils.getApp()));
//            properties.put("android_id", DeviceIdUtils.getAndroidId(Utils.getApp()));
//            properties.put("device_type", "android");
//            eventSdk.track("user_register", properties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void ATAdStatistic(String adType, String eventType) {
//        ThinkingDataEvent.clickEventTrack("topon_ad_load_statistic", "event_type", eventType, "ad_type", adType, "appVersion", AppInfoUtils.getVerName());
//    }
//
//
//}
