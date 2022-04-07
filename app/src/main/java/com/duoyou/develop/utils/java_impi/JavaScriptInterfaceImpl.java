package com.duoyou.develop.utils.java_impi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.duoyou.develop.entity.UserInfo;
import com.duoyou.develop.ui.WebViewActivity;
import com.duoyou.develop.utils.AppInfoUtils;
import com.duoyou.develop.utils.CommonUtils;
import com.duoyou.develop.utils.PermissionHelper;
import com.duoyou.develop.utils.SPManager;
import com.duoyou.develop.utils.http.HttpHeaderUtil;
import com.duoyou.develop.utils.image.DyImageUtilsKt;
import com.duoyou.develop.view.ToastHelper;
import com.duoyou_cps.appstore.util.LoginImplManager;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JavaScriptInterfaceImpl {

    public static List<Activity> activityList = new ArrayList<>();
    private Activity activity;
    private WebView webView;

    public JavaScriptInterfaceImpl(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
        startActivity();
    }

    @JavascriptInterface
    public void startWebViewPage(String url) {
        activity.runOnUiThread(() -> WebViewActivity.launch(activity, url));
    }

    @JavascriptInterface
    public void back() {
        activity.finish();
    }

    @JavascriptInterface
    public void goback() {
        activity.finish();
    }

    public void finishActivity() {
        activityList.remove(activity);
    }

    public void startActivity() {
        activityList.add(activity);
    }

    @JavascriptInterface
    public void sdkInit() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean value = SPManager.getValue(SPManager.AGREE_USER_PROTOCOL, false);
                    if (value) {
                        String jsonString = HttpHeaderUtil.getHeaderInfoJsonString();
                        webView.loadUrl(String.format("javascript:sdkInit('%s')", jsonString));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    @JavascriptInterface
//    public void checkUpdate() {
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                BuglyUtils.checkUpdate();
//            }
//        });
//    }

    @JavascriptInterface
    public void downloadTask(String taskInfo) {
        activity.runOnUiThread(() -> {
            boolean b = PermissionHelper.hasPermission(activity);
            if (b) {

            }

        });


    }


    @JavascriptInterface
    public void openBrowser(String url) {
        activity.runOnUiThread(() -> CommonUtils.openBrowser(activity, url));
    }

    @JavascriptInterface
    public void openActivity(String activityUrl) {
        activity.runOnUiThread(() -> {
            try {
                ComponentName componentName = new ComponentName(activity, activityUrl);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                activity.startActivity(intent);
            } catch (Exception e) {
                ToastHelper.showShort("页面地址有误");
            }
        });
    }

    @JavascriptInterface
    public void showToast(String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastHelper.showShort(text);
            }
        });
    }

    @JavascriptInterface
    public String getToken() {
        return UserInfo.getInstance().getAccessToken();
    }

    @JavascriptInterface
    public void clearCache() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView != null) {
                    webView.clearCache(true);
                }
                ToastHelper.showShort("缓存清除成功");
            }
        });
    }

    @JavascriptInterface
    public void launchApp(String packageName) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonUtils.startApp(packageName);
            }
        });
    }

    @JavascriptInterface
    public void finishAllWebView() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < activityList.size(); i++) {
                    activityList.get(i).finish();
                }
            }
        });
    }

//    public static void finishWebActivityToMain() {
//        DyApplication.Companion.getCurrentActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (activityList != null && activityList.size() > 0) {
//                    for (int l = activityList.size(), i = l - 1; i >= 0; i--) {
//                        Activity activity = activityList.get(i);
//                        if (activity instanceof MainActivity) {
//                            continue;
//                        }
//                        activity.finish();
//                    }
//                }
//
//            }
//        });
//
//    }

    @JavascriptInterface
    public void copyText(String text) {
        activity.runOnUiThread(() -> CommonUtils.copyText(activity, text));
    }

    @JavascriptInterface
    public void logout() {
        activity.runOnUiThread(() -> {
            if (LoginImplManager.INSTANCE.getLoginStatus()) {
                LoginImplManager.INSTANCE.logout();
            }
        });

//        if (!ChangeUserEvent.Companion.getCurrentIsChangeUser()) {
//            EventBusUtils.post(new ChangeUserEvent(""));
//            ChangeUserEvent.Companion.setCurrentIsChangeUser(true);
//        }
    }

    @JavascriptInterface
    public void shareImage(String base64Str) {
        DyImageUtilsKt.shareImage(activity, base64Str);
    }

    @JavascriptInterface
    public void saveImage(String base64Str) {
        DyImageUtilsKt.saveImage2SDCARD(base64Str);
    }

    @JavascriptInterface
    public String getChannel() {
        return AppInfoUtils.getChannel();
    }


    @JavascriptInterface
    public String getHeadInfo() {
        boolean value = SPManager.getValue(SPManager.AGREE_USER_PROTOCOL, false);
        if (value) {
            return HttpHeaderUtil.getHeaderInfoJsonStringBase64();
        }
        return null;
    }

    @JavascriptInterface
    public String getRecentAccountString() {
        return CommonUtils.getRecentAccountString(activity);
    }

    @JavascriptInterface
    public void writeRecentAccount(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String username = jsonObject.optString("username");
            String password = jsonObject.optString("password");
            CommonUtils.writeRecentAccountString(activity, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void deleteRecentAccount(String username) {
        try {
            CommonUtils.deleteAccountFromFile(activity, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    @JavascriptInterface
//    public void openQQ() {
//        activity.runOnUiThread(() -> ThirdSdkUtils.joinQQ(activity));
//    }
}
