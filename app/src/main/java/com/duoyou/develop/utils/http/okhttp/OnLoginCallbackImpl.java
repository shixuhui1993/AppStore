package com.duoyou.develop.utils.http.okhttp;


import android.util.ArrayMap;

import com.duoyou.develop.entity.UserInfo;
import com.duoyou.develop.utils.http.JSONUtils;


public class OnLoginCallbackImpl implements OkHttpCallback {

    private final OkHttpCallback okHttpCallback;

    public OnLoginCallbackImpl(OkHttpCallback okHttpCallback) {
        this.okHttpCallback = okHttpCallback;
    }

    @Override
    public void onSuccess(String result) {
        if (JSONUtils.isResponseOK(result)) {
            UserInfo userInfo = UserInfo.getInstance();
//            userInfo.setUserInfoWithJsonString(result);
            if (okHttpCallback != null) {
                okHttpCallback.onSuccess(result);
            }
            ArrayMap<String, Object> eventMap = new ArrayMap<>();
            eventMap.put("step", "login");
//            ThinkingDataEvent.eventTrack(ThinkingDataEvent.ACTIVITY_OPEN, eventMap);
        } else {
            onFailure(JSONUtils.getCode(result), JSONUtils.getMessage(result));
        }
    }

    @Override
    public void onFailure(String code, String message) {
        if (okHttpCallback != null) {
            okHttpCallback.onFailure(code, message);
        }
    }

    @Override
    public boolean isVerify() {
        return false;
    }
}
