package com.duoyou.develop.utils.http.okhttp;


import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.StringUtils;
import com.duoyou.develop.entity.UserInfo;
import com.duoyou.develop.utils.AppInfoUtils;
import com.duoyou.develop.utils.EasyAES;
import com.duoyou.develop.utils.LogUtil;
import com.duoyou.develop.utils.http.JSONUtils;
import com.duoyou.develop.view.ToastHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;


public class CallbackImpl implements Callback {

    public OkHttpCallback callback;
    private final Handler handler;
    private boolean isVerify;

    public CallbackImpl(OkHttpCallback okHttpCallback) {
        this.callback = okHttpCallback;
        if (okHttpCallback != null) {
            isVerify = okHttpCallback.isVerify();
        }
        handler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void onFailure(Call call, IOException e) {
        HttpUrl url = call.request().url();
        LogUtil.i("json__slt_onFailure", "请求地址返回：" + url + "  " + e.getMessage());

        if (e.getMessage() != null) {
            if (e.getMessage().contains("Unable to resolve host")) {
                onFailure("800", "网络连接失败", call.request().url() + "");
            } else if (e.getMessage().contains("time out")) {
                onFailure("801", "连接超时", call.request().url() + "");
            } else {
//                onFailure("700", "请求失败",call.request().url() + "");
            }

        }
    }

    @Override
    public void onResponse(Call call, okhttp3.Response response) throws IOException {
        if (!response.isSuccessful()) {
            onFailure(response.code() + "", response.message(), call.request().url() + "");
            return;
        }
        String result;
        try {
            result = response.body().string();
//            LogUtil.i("json__slt", "请求地址返回：" + response.request().url() + " " + result);
            if(StringUtils.isEmpty(result)){
                onFailure("10010", "数据返回异常", call.request().url() + "");
                return;
            }
            boolean b = result.startsWith("{") && result.endsWith("}");
            if(isVerify&&!b){
                result = EasyAES.decryptString(result);
            }
            if (StringUtils.isEmpty(result)) {
                onFailure("8888", "解密失败", call.request().url() + "");
                return;
            }

            if (AppInfoUtils.isDebug()) {
                int subLength = 1000;
                if (result.length() <= subLength) {
                    LogUtil.i("json__slt", "请求地址返回：" + response.request().url() + " " + result);
                } else {
                    LogUtil.i("json__slt", "请求地址返回：" + response.request().url() + " " + result.substring(0, subLength));
                    LogUtil.i("json__slt", result.substring(subLength));
                }
            }
            boolean responseOK = JSONUtils.isResponseOK(result);
            if (responseOK) {
                onSuccess(result);
            } else {
                String code = JSONUtils.getCode(result);
                if ("9999".equals(code) || "10000".equals(code)) {
                    UserInfo.getInstance().logout();
//                    if (!ChangeUserEvent.Companion.getCurrentIsChange
//
//                    User()) {
//                        EventBusUtils.post(new ChangeUserEvent(""));
//                        ChangeUserEvent.Companion.setCurrentIsChangeUser(true);
//                    }
                }
                String message = JSONUtils.getMessage(result);
                onFailure(code, message, call.request().url() + "");
            }

        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.i("json__slt", "请求地址返回：错误  " + e.getMessage());
        }
    }


    private void onSuccess(final String result) {
        handler.post(() -> {
            if (callback != null) {
                callback.onSuccess(result);
            }
        });
    }

    private void onFailure(final String code, final String message, String desc) {
        LogUtil.i("json__slt", "请求地址返回：" + desc + "  code = " + code+"  msg = "+message);
        handler.post(() -> {
            if (callback != null) {
                ToastHelper.showShort(message);
                callback.onFailure(code, message);
            }
        });
    }

}
