package com.duoyou.develop.utils.http.okhttp;


public abstract class MyOkHttpCallback implements OkHttpCallback {

    @Override
    public void onSuccess(String result) {
        onSuccessNoCode(result);
        onTaskFinish();
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

    public boolean isVerify() {
        return false;
    }
}
