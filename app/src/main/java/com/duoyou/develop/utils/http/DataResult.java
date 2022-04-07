package com.duoyou.develop.utils.http;

import android.text.TextUtils;

import androidx.annotation.Keep;


@Keep
public class DataResult<T> {

    private String code;
    private String status;
    private String message;

    private T data;

    public T  getData() {
        return data;
    }

    public void setData(T  data) {
        this.data = data;
    }

    public String getCode() {
        return TextUtils.isEmpty(code) ? status : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResponseOk() {
        return JSONUtils.RES_CODE_SUCCESS.equals(code) || JSONUtils.RES_CODE_SUCCESS.equals(status);
    }

    public boolean isDataNull() {
        return data == null;
    }
}