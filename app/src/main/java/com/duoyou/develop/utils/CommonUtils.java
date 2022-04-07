package com.duoyou.develop.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.StringUtils;
import com.duoyou.develop.view.ToastHelper;
import com.duoyou_cps.appstore.BuildConfig;
import com.duoyou_cps.appstore.entity.RecentAccount;
import com.duoyou_cps.appstore.util.PathUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class CommonUtils {


    public static void openBrowser(Context context, String url) {
        try {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if (TextUtils.isEmpty(url) || !url.contains("://")) {
                url = "http://" + url;
            }
            intent.setData(Uri.parse(url));
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.showShort("无法打开地址，请复制自行打开");
        }
    }


    /**
     * Return the application's name.
     *
     * @return the application's name
     */
    public static String getAppName() {
        return getAppName(AppInfoUtils.getApplication().getPackageName());
    }

    /**
     * Return the application's package name.
     *
     * @return the application's package name
     */
    public static String getAppPackageName() {
        return AppInfoUtils.getApplication().getPackageName();
    }

    /**
     * Return the application's name.
     *
     * @param packageName The name of the package.
     * @return the application's name
     */
    public static String getAppName(final String packageName) {
        try {
            PackageManager pm = AppInfoUtils.getApplication().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static int hasSimCard() {
        int status = 1;
        try {
            Context context = AppInfoUtils.getApplication();
            TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    status = 0; // 没有SIM卡
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    status = 0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static void launchAppDetailsSettings(Context context, final String packageName) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 二合一接口
     */
    public static void startQQKeFu(Context context, String qqNum) {
        try {
            if (TextUtils.isEmpty(qqNum)) {
                qqNum = "1990554466";
            }
            String kefuString;
            if (!qqNum.startsWith("mqqwpa://")) {
                kefuString = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum;
            } else {
                kefuString = qqNum;
            }
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(kefuString)));
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.showShort("请先安装QQ客户端");
        }
    }

    /**
     * 启动应用
     */
    public static void startApp(String packageName) {
        try {
            PackageManager pm = AppInfoUtils.getApplication().getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            AppInfoUtils.getApplication().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
    public static void installApk(String filePath) {
        Context context = AppInfoUtils.getApplication();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { /* Android N 写法*/
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".myfileprovider", new File(filePath));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else { /* Android N之前的老版本写法*/
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static long getAppInstallTime() {
        try {
            PackageManager pm = AppInfoUtils.getApplication().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(AppInfoUtils.getApplication().getPackageName(), 0);
            return pi.firstInstallTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断下载的apk是否完整
     */
    public static boolean isApkOk(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
            PackageManager pm = AppInfoUtils.getApplication().getPackageManager();
            PackageInfo pkgInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAppInstalled(String packageName) {
        return getPackageInfo(packageName) != null;
    }

    public static PackageInfo getPackageInfo(String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        //包管理操作管理类
        PackageManager pm = AppInfoUtils.getApplication().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUUID() {
        try {
            UUID uuid = UUID.randomUUID();
            return uuid.toString().replaceAll("-", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() + "";
    }

    public static boolean copyText(Context context, String text) {
        return copyText(context,text,true);
    }

    public static boolean copyText(Context context, String text, boolean showTip) {
        try {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", text);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            if (!StringUtils.isEmpty(text) && showTip) {
                ToastHelper.showShort("复制成功");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (!StringUtils.isEmpty(text)&& showTip) {
                ToastHelper.showShort("复制失败");
            }
        }
        return false;
    }

    /**
     * 获取剪切板上的内容
     */
    @Nullable
    public static String getClipboardContent(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            ClipData data = cm.getPrimaryClip();
            if (data != null && data.getItemCount() > 0) {
                ClipData.Item item = data.getItemAt(0);
                if (item != null) {
                    CharSequence sequence = item.coerceToText(context);
                    if (sequence != null) {
                        return sequence.toString();
                    }
                }
            }
        }
        return null;
    }


    private static long lastClickTime;

    public static boolean isClickFast() {
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime < 200) {
            return true;
        }
        lastClickTime = currentClickTime;
        return false;
    }
    public static String getRecentAccountString(Context context) {
        try {
            String filePath = PathUtils.getUserFilePath() + "game_sdk_u.mark";
            String jsonString = FileUtil.readString(filePath, "utf-8");
            if (TextUtils.isEmpty(jsonString)) {
                jsonString = SPManager.getValue(SPManager.RECENT_ACCOUNT_STRING, "");
            }
            return jsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeRecentAccountString(Context context, String um, String pd) {
        try {
            RecentAccount recentAccount = new RecentAccount();
            recentAccount.setUserName(um);
            recentAccount.setPassword(pd);
            String filePath = PathUtils.getUserFilePath() + "game_sdk_u.mark";
            String jsonString = FileUtil.readString(filePath, "utf-8");
            if (TextUtils.isEmpty(jsonString)) {
                jsonString = SPManager.getValue(SPManager.RECENT_ACCOUNT_STRING, "");
            }
            List<RecentAccount> list = new ArrayList<>();
            if (!TextUtils.isEmpty(jsonString)) {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i ++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    String username = jsonObject.optString("username");
                    String password = jsonObject.optString("password");
                    RecentAccount account = new RecentAccount();
                    account.setUserName(username);
                    account.setPassword(password);
                    list.add(account);
                }

                RecentAccount sameAccount = null;
                for (int i = 0; i < list.size(); i ++) {
                    RecentAccount account = list.get(i);
                    if (account.getUserName().equals(um)) {
                        sameAccount = account;
                        break;
                    }
                }

                if (sameAccount != null) {
                    sameAccount.setPassword(recentAccount.getPassword());
                } else {
                    list.add(0, recentAccount);
                }
            } else {
                list.add(recentAccount);
            }

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                RecentAccount account = list.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", account.getUserName());
                jsonObject.put("password", account.getPassword());
                jsonArray.put(jsonObject);
            }
            if (jsonArray.length() > 0) {
                String accountString = jsonArray.toString();
                FileUtil.writeString(filePath, accountString, "utf-8");
                SPManager.putValue(context, SPManager.RECENT_ACCOUNT_STRING, accountString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<RecentAccount> getRecentAccountList(Context context) {
        List<RecentAccount> recentAccountList = new ArrayList<>();
        try {
            String filePath = PathUtils.getUserFilePath() + "game_sdk_u.mark";
            String jsonString = FileUtil.readString(filePath, "utf-8");
            if (!TextUtils.isEmpty(jsonString)) {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    RecentAccount recentAccount = new RecentAccount();
                    recentAccount.setUserName(jsonObject.optString("username"));
                    recentAccount.setPassword(jsonObject.optString("password"));
                    recentAccountList.add(recentAccount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recentAccountList;
    }

    public static void deleteAccountFromFile(Context context, String username) {
        try {
            List<RecentAccount> list = getRecentAccountList(context);
            Iterator<RecentAccount> iterator = list.iterator();
            while (iterator.hasNext()) {
                RecentAccount recentAccount = iterator.next();
                if (username.equals(recentAccount.getUserName())) {
                    iterator.remove();//使用迭代器的删除方法删除
                }
            }
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                RecentAccount account = list.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", account.getUserName());
                jsonObject.put("password", account.getPassword());
                jsonArray.put(jsonObject);
            }
            String jsonString = jsonArray.toString();
            String filePath = PathUtils.getUserFilePath() + "game_sdk_u.mark";
            FileUtil.writeString(filePath, jsonString, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getCurrentTimeString() {
        long second = System.currentTimeMillis() / 1000;
        return second + "";
    }
}
