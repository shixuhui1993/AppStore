package com.duoyou_cps.appstore.util

import android.util.Log
import com.duoyou_cps.appstore.util.httputils.Tip
import java.text.DecimalFormat


fun getAmountString(tar: Double): String {
    if (tar % 1 == 0.0) {
        return tar.toInt().toString()
    }
    val value = DecimalFormat("#.##")
    return value.format(tar)
}

fun logi(tar: String, content: String?) {
    content?.let {
        Log.i(tar, it)
    }
}

fun logi(content: String?) {
    logi("log_info", content)
}
fun showToast(content: String) {
    Tip.show(content)
}