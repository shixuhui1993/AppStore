//package com.duoyou.develop.utils.thinkingdata;
//
//import androidx.fragment.app.Fragment;
//
//import com.blankj.utilcode.util.Utils;
//import com.duoyou.develop.utils.AppInfoUtils;
//import com.duoyou_cps.appstore.config.ThirdSdkConfig;
//import com.duoyou.develop.entity.UserInfo;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.thinkingdata.android.TDConfig;
//import cn.thinkingdata.android.ThinkingAnalyticsSDK;
//
//
//public class ThinkingDataUtil {
//    private static volatile ThinkingDataUtil dataUtil;
//    private ThinkingAnalyticsSDK instance;
//
//    private ThinkingDataUtil() {
//    }
//
//    public static synchronized ThinkingDataUtil getInstance() {
//        if (dataUtil == null) {
//            dataUtil = new ThinkingDataUtil();
//        }
//        return dataUtil;
//
//    }
//
//    private boolean isInited;
//
//    public void init() {
//        // 在主线程中初始化 SDK
//        // 获取 TDConfig 实例
//        TDConfig config = TDConfig.getInstance(Utils.getApp(), ThirdSdkConfig.THINKIND_DATA_APP_ID, ThirdSdkConfig.THINKIND_DATA_TA_SERVER_URL);
//
//        // 初始化 SDK
//        instance = ThinkingAnalyticsSDK.sharedInstance(config);
//        instance.setNetworkType(ThinkingAnalyticsSDK.ThinkingdataNetworkType.NETWORKTYPE_DEFAULT);
//        ThinkingAnalyticsSDK.enableTrackLog(true);
//        NtpTime.startCalibrateTime();
//        // 设置自动采集
//        setAutoTrack();
//        ThinkingDataEvent.initEventSdk(instance);
//        isInited = true;
//    }
//
//    public void setFragmentTrack(Fragment fragmentTrack) {
//        if (!isInited) {
//            return;
//        }
//
//        instance.trackViewScreen(fragmentTrack);
//    }
//
//
//    public void setAutoTrack() {
//        // 设置公共事件属性
//        try {
//            JSONObject superProperties = new JSONObject();
//            superProperties.put("SUPER_PROPERTY_CHANNEL", AppInfoUtils.getChannel());
//            superProperties.put("channel", AppInfoUtils.getChannel());
//            instance.setSuperProperties(superProperties);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //设置完访客ID与公共属性后，再开启自动采集
//        List<ThinkingAnalyticsSDK.AutoTrackEventType> eventTypeList = new ArrayList<>();
//        //APP安装事件
//        eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_INSTALL);
//
//        //APP启动事件
//        eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_START);
//
//        //APP关闭事件
//        eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_END);
//
//        //APP浏览页面事件
//        eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_VIEW_SCREEN);
//
//        //APP点击控件事件
//        eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CLICK);
//
//        //APP崩溃事件
//        eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CRASH);
//
//        instance.enableAutoTrack(eventTypeList);
//    }
//
//
//    public void loginOrOut() {
//        if (instance == null) {
//            return;
//        }
//        if (UserInfo.getInstance().isLogin()) {
//            instance.login(UserInfo.getInstance().getUid() + "");
//            ThinkingDataEvent.login();
//        } else {
//            instance.logout();
//        }
//    }
//}
