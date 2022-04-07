package com.duoyou.develop.utils.http.okhttp;

public interface OkHttpCallback{

    void onSuccess(String result) ;

    void onFailure(String code, String message);

    boolean isVerify();

}
