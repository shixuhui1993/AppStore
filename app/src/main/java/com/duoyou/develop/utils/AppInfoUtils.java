package com.duoyou.develop.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.duoyou_cps.appstore.BuildConfig;

import java.util.Objects;

public class AppInfoUtils {
    public static boolean isDebug() {
        return AppUtils.isAppDebug();
    }

    public static boolean isAppForeground() {
        return AppUtils.isAppForeground();
    }

    public static Application getApplication() {
        return Utils.getApp();
    }

    public static String getChannel() {
        String cpsChannel = ChannelUtil.getCpsChannel();
        if (!StringUtils.isEmpty(cpsChannel)) {
            return cpsChannel;
        }
        String umeng_channel = getAppMetaData(getApplication(), "UMENG_CHANNEL");
        if (StringUtils.isEmpty(umeng_channel)) {
//            return BuildConfig.UMENG_CHANNEL;
            return "CPS_35852632";
        }
        return umeng_channel;
    }

    public static boolean isWhitePack() {
        return "white".equals(getChannel());
    }


    public static boolean isXiaomiPhone() {
        return Build.BRAND.equalsIgnoreCase("xiaomi") || Build.BRAND.equalsIgnoreCase("redmi");
    }

    public static String getVerCodeStr() {
        return getVerCode() + "";
    }

    public static int getVerCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static String getVerName() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getApplicationInfo().packageName, PackageManager.GET_META_DATA);
                if (applicationInfo.metaData != null && applicationInfo.metaData.get(key) != null) {
                    resultData = Objects.requireNonNull(applicationInfo.metaData.get(key)).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }
}
