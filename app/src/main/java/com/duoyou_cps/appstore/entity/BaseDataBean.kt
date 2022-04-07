package com.duoyou_cps.appstore.entity

class BaseDataBean<T> {
    var status_code: Int = 0
    var message: String? = null
    var data: T? = null
}

