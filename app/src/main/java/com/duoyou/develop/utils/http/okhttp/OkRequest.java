package com.duoyou.develop.utils.http.okhttp;

import static com.duoyou.develop.utils.http.HttpUaUtilKt.getUserAgent;

import android.util.ArrayMap;

import com.duoyou.develop.entity.UserInfo;
import com.duoyou.develop.utils.AppInfoUtils;
import com.duoyou.develop.utils.EasyAES;
import com.duoyou.develop.utils.LogUtil;
import com.duoyou.develop.utils.http.HttpHeaderUtil;
import com.duoyou_cps.appstore.util.httputils.HttpUrlKtKt;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class OkRequest {

    public static final long DEFAULT_MILLISECONDS = 30;      //默认的超时时间
    public static final long DEFAULT_CONNECT_TIME_OUT = 15;

    public static void post(Map<String, String> map, String url, OkHttpCallback callback) {
        post(map, url, false, callback);
    }

    public static void post(Map<String, String> map, String url, boolean isVerify, OkHttpCallback callback) {
        if (map == null) {
            map = new ArrayMap<>();
        }
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = getHttpClient();
        // 添加公用参数
        addCommonParams(map);
        // 获取完整地址
        String httpUrl = HttpUrlKtKt.getUrlWithHost(url);
        LogUtil.i("json__req", "请求参数：" + httpUrl + "   " + new Gson().toJson(map));

        Request.Builder requestBuilder = new Request.Builder();
        //3.添加公用头
        addCommonHeader(requestBuilder, getCommonHeader());
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        // 设置请求参数
        if (!map.isEmpty()) {
            if (url.contains("android_") || isVerify) { // 如果是这个下面的路由，全部加密
                JSONObject jsonObject = new JSONObject(map);
                String paramsString = EasyAES.encryptString(jsonObject.toString());
                bodyBuilder.add("params", paramsString);
//                LogUtil.i("json__req", "加密请求：" + httpUrl + " " + jsonObject.toString());
            } else {
                for (String key : map.keySet()) {
                    String value = map.get(key);
                    if (value != null) {
                        bodyBuilder.add(key, value);
                    }
                }
            }
        }
        RequestBody requestBody = bodyBuilder.build();
        requestBuilder.post(requestBody);
        requestBuilder.url(httpUrl);
        Request request = requestBuilder.build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new CallbackImpl(callback));
    }

    public static void get(Map<String, String> map, String url, OkHttpCallback callback) {
        get(map, url, false, callback);
    }

    public static void get(Map<String, String> map, String url, boolean isVer, OkHttpCallback callback) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = getHttpClient();
        // 获取完整地址
        String httpUrl = HttpUrlKtKt.getUrlWithHost(url);
        // 添加公用参数
        addCommonParams(map);
        String httpUrlWithParams;
        if (url.contains("android_") || isVer) { // 如果是android_的路由统一加密
            JSONObject jsonObject = new JSONObject(map);
            String paramsString = EasyAES.encryptString(jsonObject.toString());
            Map<String, String> mapParams = new HashMap<>();
            mapParams.put("params", paramsString);
            httpUrlWithParams = HttpUrlKtKt.getUrlWithMap(httpUrl, mapParams);
        } else {
            httpUrlWithParams = HttpUrlKtKt.getUrlWithMap(httpUrl, map);
        }
        addCommonParams(map);
        LogUtil.i("json__req", "请求地址：" + httpUrlWithParams);
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        final Request.Builder builder = new Request.Builder();
        //3.添加公用头
        addCommonHeader(builder, getCommonHeader());
        Request request = builder.url(httpUrlWithParams).method("GET", null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new CallbackImpl(callback));
    }

    public static void requestGet(Map<String, String> map, String url, OkHttpCallback callback) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = getHttpClient();
        // 获取完整地址
        String httpUrl = HttpUrlKtKt.getUrlWithHost(url);

        String httpUrlWithParams = HttpUrlKtKt.getUrlWithMap(httpUrl, map);
        LogUtil.i("json__req", "请求地址：" + httpUrlWithParams);
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        final Request.Builder builder = new Request.Builder();

        Request request = builder.url(httpUrlWithParams).method("GET", null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new CallbackImpl(callback));
    }

    public static void postJson(Map<String, String> map, String url, OkHttpCallback callback) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = getHttpClient();
        // 添加公用参数
        addCommonParams(map);
        // 获取完整地址
        String httpUrl = HttpUrlKtKt.getUrlWithHost(url);
        Request.Builder requestBuilder = new Request.Builder();
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        JSONObject jsonObject = new JSONObject(map);
        String toString = jsonObject.toString();
        LogUtil.i("json__req", "请求参数：" + url + "  " + toString);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"), toString);
        requestBuilder.post(requestBody);
        requestBuilder.url(httpUrl);
        Request request = requestBuilder.build();

        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new CallbackImpl(callback));
    }

    public static OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS);
        builder.connectTimeout(DEFAULT_CONNECT_TIME_OUT, TimeUnit.SECONDS);
        if (!AppInfoUtils.isDebug()) {
            builder.proxy(Proxy.NO_PROXY);
        }
        return builder.build();
    }

    public static Map<String, String> getCommonHeader() {
        Map<String, String> map = new HashMap<>();
        // 添加公用头
        try {
            String headerInfoJsonStringBase64 = HttpHeaderUtil.getHeaderInfoJsonStringBase64();
            map.put("headerinfo", headerInfoJsonStringBase64);
            map.put("User-Agent", getUserAgent());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public static void addCommonHeader(Request.Builder builder, Map<String, String> map) {
        if (map == null || builder == null) {
            return;
        }
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value != null) {
                builder.addHeader(key, value);
            }
        }
    }

    public static void addCommonParams(Map<String, String> map) {
        if (UserInfo.getInstance().isLogin())
            if (!map.containsKey("token")) {
                map.put("token", UserInfo.getInstance().getAccessToken());
            }
    }

}
