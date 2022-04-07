package com.duoyou_cps.appstore.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.duoyou_cps.appstore.db.DownloadTask

class AppInstalledReceiver(
    private val task: DownloadTask,
    private var listener: AppInstallListener?
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        val action = intent.action

        if (Intent.ACTION_PACKAGE_ADDED == action || Intent.ACTION_PACKAGE_REPLACED == action) {
            val data = intent.dataString
            val scheme = intent.scheme + ":"
            data?.let {
                val packageName = it.replace(scheme, "")
                if (task.packageName == packageName) {
                    task.installed = 1
                    task.save()
                    listener?.appInstallSuccess(task)
                    context?.unregisterReceiver(this)
                }
            }

        }
    }
}

fun regisBroadcastReceiver(
    activity: Activity?,
    task: DownloadTask,
    listener: AppInstallListener
): BroadcastReceiver {
    val intentFilter = IntentFilter()
    intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
    intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
    intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
    intentFilter.addDataScheme("package")
    val receiver = AppInstalledReceiver(task, listener)
    activity?.registerReceiver(receiver, intentFilter)
    return receiver
}