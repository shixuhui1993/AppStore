package com.duoyou_cps.appstore.api

import HttpCallback
import com.duoyou_cps.appstore.util.httputils.GET_CODE
import httpRequest

fun <T> sendVerifyCode(params: MutableMap<String, Any?>, clazz: Class<T>, callback: HttpCallback<T>) {
    httpRequest(GET_CODE, params, clazz, callback)
}