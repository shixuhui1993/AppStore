package com.duoyou_cps.appstore.util.httputils

import android.net.Uri
import android.text.TextUtils
import com.duoyou.develop.entity.UserInfo
import com.duoyou.develop.utils.AppInfoUtils
import com.duoyou.develop.utils.SPManager
import com.duoyou_cps.appstore.util.httputils.HttpUrlKt.Companion.HOST


const val HOST_VAL = " http://wap.duoyou.com/index.php/"
const val HOST_H5_VAL = "http://gbox.h5.dysdk.com/#/"


const val HOST_VAL_T = "http://t-wap.duoyou.com/index.php/"
const val HOST_H5_VAL_T = "http://t-wap.duoyou.com/"
const val isDebug = true

class HttpUrlKt {

    companion object {
        var HOST = ""
        var HOST_H5 = ""

        init {
            initHttpHost()
        }

        fun initHttpHost() {
            val isDebug = SPManager.getValue(SPManager.SERVER_RELEASE, AppInfoUtils.isDebug())
            if (isDebug) {
                HOST = HOST_VAL_T
                HOST_H5 = HOST_H5_VAL_T
//                HOST = HOST_VAL
//                HOST_H5 = HOST_H5_VAL
            } else {
                HOST = HOST_VAL
                HOST_H5 = HOST_H5_VAL
            }
        }

        fun getH5HttpUrl(url: String): String {
            if (url.startsWith("http")) {
                return url
            }
            return HOST_H5 + url
        }

        fun getH5HttpUrlMarginTop(url: String): String {
            var extra = "?setMarginTop=1"
            if (url.contains("?")) {
                extra = "&setMarginTop=1"
            }
            if (url.startsWith("http")) {
                return "$url$extra"
            }
            return "$HOST_H5$url$extra"
        }
    }
}

/**
 *  发送验证码
 */
const val GET_CODE = "member/send_verification"
/**
 *  手机号登录
 */
const val LOGIN_PHONE = "auth/phone_login"
/**
 *  账号登录
 */
const val LOGIN_ACCOUNT = "auth/account_login"

/**
 *  修改密码
 */
const val USER_CHANGE_PSW = "member/change_pass"
/**
 *  绑定手机号
 */
const val USER_BIND_PHONE = "member/bind_phone"

/**
 *  用户信息
 */
const val USER_MEMBER_INFO = "member/get_member_info"

/**
 *  客服信息
 */
const val SERVICE_INFO = "app/customer"


/**
 * ------------------------------------H5--------------------------------------------
 */
const val APP_HOME = "app/#/index"
const val APP_CLASSIFY = "app/#/classify"


fun getUrlWithHost(partUrl: String): String? {
    if (!TextUtils.isEmpty(partUrl)) {
        if (partUrl.startsWith("http://") || partUrl.startsWith("https://")) {
            return partUrl
        }
    }
    return getUrlWithHost(HOST, partUrl)
}

fun getUrlWithHost(host: String, partUrl: String): String? {
    return host + partUrl
}

fun getUrlWithMap(
    httpUrl: String,
    map: Map<String?, String?>?
): String? {
    val queryBuilder = StringBuilder(httpUrl)
    if (!httpUrl.contains("?")) {
        queryBuilder.append("?")
    } else if (!httpUrl.endsWith("&")) {
        queryBuilder.append("&")
    }
    if (map != null && map.isNotEmpty()) {
        for (key in map.keys) {
            val value = map[key]
            if (!TextUtils.isEmpty(key) && value != null) {
                queryBuilder.append(Uri.encode(key, "UTF-8"))
                    .append("=")
                    .append(Uri.encode(value, "UTF-8"))
                    .append("&")
            }
        }
    }
    if (queryBuilder[queryBuilder.length - 1] == '&') {
        queryBuilder.deleteCharAt(queryBuilder.length - 1)
    }
    if (queryBuilder[queryBuilder.length - 1] == '?') {
        queryBuilder.deleteCharAt(queryBuilder.length - 1)
    }
    return queryBuilder.toString()
}

fun getUrlWithParams(url: String): String? {
    val paramsMap: MutableMap<String?, String?> =
        HashMap()
    paramsMap["token"] = UserInfo.getInstance().accessToken
    return getUrlWithMap(url, paramsMap)
}