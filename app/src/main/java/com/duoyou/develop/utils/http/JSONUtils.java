package com.duoyou.develop.utils.http;

import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JSONUtils {

    public static final String RES_CODE_SUCCESS = "200";

    public static JSONObject formatJSONObject(String result) {
        try {
            return new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject formatJSONObjectWithData(String result) {
        try {
            JSONObject json = new JSONObject(result);
            return json.optJSONObject("data") == null ? new JSONObject() : json.optJSONObject("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONArray formatJSONArray(String result) {
        try {
            JSONObject json = new JSONObject(result);
            return json.optJSONArray("data") == null ? new JSONArray() : json.optJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }


    public static boolean isResponseOK(String result) {
        if (StringUtils.isEmpty(result)) {
            return false;
        }
        try {
            JSONObject json = new JSONObject(result);
            if (RES_CODE_SUCCESS.equals(json.optString("status_code")) || RES_CODE_SUCCESS.equals(json.optString("statusCode")) || 200 == json.optInt("statusCode")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCode(String result) {
        try {
            JSONObject json = new JSONObject(result);
            return json.optString("status_code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getMessage(String result) {
        try {
            JSONObject json = new JSONObject(result);
            return json.optString("message");
        } catch (Exception e) {
//			e.printStackTrace();
        }
        return result;
    }

    public static String getMessageWithCode(String result) {
        try {
            JSONObject json = new JSONObject(result);
            return String.format("(code = %s)%s", json.optString("status_code"), json.optString("message"));
        } catch (Exception e) {
//			e.printStackTrace();
        }
        return result;
    }

    public static String getToastMessage(String result) {
        try {
            if (isResponseOK(result)) {
                return getMessage(result);
            } else {
                return getMessageWithCode(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static DataResult fromJson(String json, Class clazz) {
        try {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            Gson gson = new GsonBuilder().serializeNulls().create();
            Type objectType = type(DataResult.class, clazz);
            return gson.fromJson(json, objectType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJsonObject(String json, Class<T> t) {
        try {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            Gson gson = new GsonBuilder().serializeNulls().create();
            return gson.fromJson(json, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            Gson gson = new GsonBuilder().serializeNulls().create();
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
