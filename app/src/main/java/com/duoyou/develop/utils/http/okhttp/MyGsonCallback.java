package com.duoyou.develop.utils.http.okhttp;


import com.duoyou.develop.utils.http.JSONUtils;
import com.duoyou.develop.view.ToastHelper;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class MyGsonCallback implements OkHttpCallback {


    @Override
    public void onSuccess(String result) {
        JSONObject json;
        try {
            json = new JSONObject(result);
            if (JSONUtils.RES_CODE_SUCCESS.equals(json.optString("status_code"))) {
                onSuccessNoCode(json.optString("data"));
            } else {
                ToastHelper.showShort(json.optString("message"));
                onFailureNoCode();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onFailureNoCode();
        } finally {
            onTaskFinish();
        }
    }


    public abstract void onSuccessNoCode(String result);

    public void onFailureNoCode() {

    }

    @Override
    public void onFailure(String code, String message) {
        onFailureNoCode();
        onTaskFinish();
    }

    public void onTaskFinish() {

    }

    private final boolean isVerify;

    public MyGsonCallback() {
        isVerify = true;
    }

    public MyGsonCallback(boolean isVerify) {
        this.isVerify = isVerify;
    }

    @Override
    public boolean isVerify() {
        return isVerify;
    }
}
