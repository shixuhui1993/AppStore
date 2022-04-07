package com.duoyou.develop.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ChannelUtil {


    private static final String CHANNEL_KEY = "CHANNEL";
    private static String DUO_YOU_SDK_CHANNEL = "";
    private static String CPS_CODE_VALUE = "";


    public static String getDuoYouSdkChannel(Context context) {
        if (TextUtils.isEmpty(DUO_YOU_SDK_CHANNEL)) {
            try {
                String jsonStr = getChannelFromApk(context);
                if (TextUtils.isEmpty(jsonStr)) {
                    return DUO_YOU_SDK_CHANNEL;
                }
                JSONObject jsonObject = new JSONObject(jsonStr);
                DUO_YOU_SDK_CHANNEL = jsonObject.optString(CHANNEL_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DUO_YOU_SDK_CHANNEL == null ? "" : DUO_YOU_SDK_CHANNEL;
    }

    public static String getCpsChannel() {
        if (TextUtils.isEmpty(CPS_CODE_VALUE)) {
            try {
                Context context = Utils.getApp();
                String jsonStr = getChannelFromApk(context);
                if (TextUtils.isEmpty(jsonStr)) {
                    return CPS_CODE_VALUE;
                }
                JSONObject jsonObject = new JSONObject(jsonStr);
                String INVITE_CODE_KEY = "CPS_CHANNEL";
                CPS_CODE_VALUE = jsonObject.optString(INVITE_CODE_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return CPS_CODE_VALUE == null ? "" : CPS_CODE_VALUE;
    }

    /**
     * 从apk中获取版本信息
     *
     * @param context
     * @return
     */
    private static String getChannelFromApk(Context context) {
        //从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/dychannel";
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(zipfile.getInputStream(entry)));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    ret = sb.toString();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }


    /**
     * 通过packageName判断当前apk是否有dychannel目录
     *
     * @return
     */
    public static boolean checkChannelFromApk(Context context, String packageName) {

        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appinfo = packageManager
                    .getApplicationInfo(packageName, 0);

            //从apk包中获取
            String sourceDir = appinfo.sourceDir;
            //默认放在meta-inf/里， 所以需要再拼接一下
            String key = "META-INF/dychannel";
            String ret = "";
            ZipFile zipfile = null;
            try {
                zipfile = new ZipFile(sourceDir);
                Enumeration<?> entries = zipfile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String entryName = entry.getName();
                    if (entryName.startsWith(key)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (zipfile != null) {
                    try {
                        zipfile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }

    public static boolean isXiaoMi() {
        return AppInfoUtils.getChannel().equals("XIAOMI");
    }

    public static boolean isOPPO() {
        return AppInfoUtils.getChannel().equals("OPPO");
    }


}
