package com.duoyou_cps.appstore.entity;

import com.duoyou.develop.utils.EasyAES;

public class RecentAccount {
    private String userName;
    private String password;

    public String getUserName() {

        return EasyAES.decryptString(userName);
    }

    public void setUserName(String userName) {
        this.userName = EasyAES.encryptString(userName);
    }

    public String getPassword() {

        return EasyAES.decryptString(password);
    }

    public void setPassword(String password) {
        this.password = EasyAES.encryptString(password);
    }
}
