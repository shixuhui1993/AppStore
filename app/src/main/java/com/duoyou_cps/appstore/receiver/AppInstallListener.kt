package com.duoyou_cps.appstore.receiver

import com.duoyou_cps.appstore.db.DownloadTask

interface AppInstallListener {
    fun appInstallSuccess(task: DownloadTask)
}