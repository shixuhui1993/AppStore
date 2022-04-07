package com.duoyou.develop.utils.http

import android.webkit.WebSettings
import com.duoyou.develop.application.DyApplication
import com.duoyou.develop.utils.LogUtil

fun getUserAgent(): String {
    val userAgent: String = try {
        WebSettings.getDefaultUserAgent(DyApplication.getContext())
    } catch (e: Exception) {
        System.getProperty("http.agent").toString()
    }
    //调整编码，防止中文出错
    val sb = StringBuffer()
    var i = 0
    val length = userAgent.length
    while (i < length) {
        val c = userAgent[i]
        if (c <= '\u001f' || c >= '\u007f') {
            sb.append(String.format("\\u%04x", c.toInt()))
        } else {
            sb.append(c)
        }
        i++
    }
    val toString = sb.toString()
    LogUtil.i("getUA = "+"   "+toString)
    return toString
}