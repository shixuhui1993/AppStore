package com.duoyou_cps.appstore.util

import com.duoyou.develop.entity.UserInfo
import com.duoyou_cps.appstore.impi.ILoginStatusChange

object LoginImplManager {
    private var loginStatus = UserInfo.getInstance().isLogin
    private val listeners by lazy { mutableListOf<ILoginStatusChange?>() }

    fun addListener(listener: ILoginStatusChange) {
        listeners.add(listener)
    }

    fun removeListener(listener: ILoginStatusChange) {
        listeners.remove(listener)
    }

    fun clearListener() {
        listeners.clear()
    }


    fun login() {
        loginStatus = true
        for (listener in listeners) {
            listener?.login()
        }
    }

    fun logout() {
        if (!loginStatus) {
            return
        }
        UserInfo.getInstance().logout()
        for (listener in listeners) {
            listener?.logout()
        }
        loginStatus = false
    }

    fun getLoginStatus(): Boolean {
        return loginStatus
    }

}