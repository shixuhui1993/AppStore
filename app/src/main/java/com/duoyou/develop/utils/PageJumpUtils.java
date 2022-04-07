package com.duoyou.develop.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;
import com.duoyou.develop.ui.WebViewActivity;


public class PageJumpUtils {
    public static String APP_SHAME = "dcmh://";
    private static Intent lastIntent;
    private static String jumpUrl;
    private static String lastUrl;


    public static void jumpByUrl(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (CommonUtils.isClickFast() && url.equals(lastUrl)) {
            return;
        }
        lastUrl = url;
        jumpUrl = null;
        LogUtil.i("json", "url = " + url);
        try {
            Uri newUrl = Uri.parse(url);
            if (url.startsWith("http://") || url.startsWith("https://")) {
                String openType = newUrl.getQueryParameter("opentype");

                if ("webout".equals(openType)) {
                    CommonUtils.openBrowser(context, url);
                } else { // 为空跳转到内部浏览器,或者为web
                    WebViewActivity.launch(context, url);
                }
                return;
            }
            if (!url.startsWith(APP_SHAME)) {
                return;
            }
            String path = getUrlPath(url);
            switch (path) {


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void jumpByUri(Context context, Uri uri) {
        if (uri != null) {
            jumpByUrl(context, uri.toString());
        }
    }


    public static boolean launchBrowser(Context context, String url) {
        try {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
                context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getUrlPath(String url) {
        if (StringUtils.isEmpty(url))
            return "";
        try {
            if (url.contains("?")) {
                String[] split = url.split("\\?");
                String string = split[0];
                if (string.contains("/")) {
                    String[] split1 = string.split("/");
                    return split1[split1.length - 1];
                }
            }
            return url.replace(APP_SHAME, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean jumpByIntent(Context context) {
        if (lastIntent != null) {
            try {
                context.startActivity(lastIntent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            lastIntent = null;
        }
        return false;
    }

    public static boolean jumpByUrl(Context context) {
        if (jumpUrl != null) {
            jumpByUrl(context, jumpUrl);
            return true;
        }
        return false;
    }

    public static void setJumpUrl(String jumpUrl) {
        PageJumpUtils.jumpUrl = jumpUrl;
    }

    public static Intent getLastIntent() {
        return lastIntent;
    }

    public static void setLastIntent(Intent lastIntent) {
        PageJumpUtils.lastIntent = lastIntent;
    }


}
