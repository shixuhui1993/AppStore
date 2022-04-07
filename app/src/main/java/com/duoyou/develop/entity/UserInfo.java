package com.duoyou.develop.entity;

import androidx.databinding.BaseObservable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.duoyou.develop.utils.SPLoginManager;
import com.duoyou.develop.utils.http.JSONUtils;

import org.json.JSONObject;

public class UserInfo extends BaseObservable {

    private static UserInfo instance;
    private String access_token;
    private String mobile;
    private String headerpic;
    private String nickname;
    private int uid;
    private int is_new;
    private long regist_time;
    private boolean isGetUserPacket;


    public boolean isGetUserPacket() {
        if (isGetUserPacket) {
            return true;
        }
        isGetUserPacket = SPLoginManager.getValue(SPLoginManager.USER_PACKET_GET, false);
        return isGetUserPacket;
    }

    public void getUserPacket() {
        SPLoginManager.putValue(SPLoginManager.USER_PACKET_GET, true);
        isGetUserPacket = true;
    }

    private UserInfo() {
    }

    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }

    public boolean isLogin() {
        if (!StringUtils.isEmpty(access_token)) {
            return true;
        }
        String userInfoJson = SPLoginManager.getValue(SPLoginManager.USER_JSON, "");
        if (!StringUtils.isEmpty(userInfoJson)) {
            updateUserInfo(userInfoJson);
            return true;
        }
        return false;
    }

    public boolean isNewUser() {
        if (is_new == 2) {
            return false;
        }
        if (TimeUtils.isToday(regist_time * 1000)) {
            return true;
        } else {
            is_new = 2;
            updateUserInfo(this);
        }
        return false;
    }

    public void logout() {
        access_token = null;
        mobile = null;
        headerpic = null;
        nickname = null;
        uid = 0;
        is_new = 0;
        regist_time = 0;
        SPLoginManager.clear();
    }

    public void updateUserInfo(UserInfo info) {
        access_token = info.access_token;
        mobile = info.mobile;
        headerpic = info.headerpic;
        nickname = info.nickname;
        uid = info.uid;
        is_new = info.is_new;
        regist_time = info.regist_time;
        SPLoginManager.putValue(SPLoginManager.USER_JSON, GsonUtils.toJson(this));
    }

    public void updateUserInfo(String userInfoJson) {
        JSONObject jsonObject = JSONUtils.formatJSONObject(userInfoJson);
        access_token = jsonObject.optString("access_token");
        mobile = jsonObject.optString("mobile");
        headerpic = jsonObject.optString("headerpic");
        nickname = jsonObject.optString("nickname");
        uid = jsonObject.optInt("uid");
        is_new = jsonObject.optInt("is_new");
        regist_time = jsonObject.optLong("regist_time");
        SPLoginManager.putValue(SPLoginManager.USER_JSON, userInfoJson);
    }

    public void login() {
        String userInfoJson = SPLoginManager.getValue(SPLoginManager.USER_JSON, "");
        if (!StringUtils.isEmpty(userInfoJson)) {
            updateUserInfo(userInfoJson);
        }
    }

    public static void setInstance(UserInfo instance) {
        UserInfo.instance = instance;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getMobile() {
        return mobile;
    }
    public boolean isBindMobile(){
        return !StringUtils.isEmpty(mobile);
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
////        notifyObservers();
//        notifyPropertyChanged();
    }

    public String getHeaderpic() {
        return headerpic;
    }

    public void setHeaderpic(String headerpic) {
        this.headerpic = headerpic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public long getRegist_time() {
        return regist_time;
    }

    public void setRegist_time(long regist_time) {
        this.regist_time = regist_time;
    }
}
