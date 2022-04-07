package com.duoyou.develop.application

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Bundle
import com.blankj.utilcode.util.ProcessUtils
import com.duoyou.develop.utils.SPManager
import com.duoyou_cps.appstore.util.PathUtils
import com.duoyou_cps.appstore.util.httputils.RxHttpManager
import com.orm.SugarContext

class DyApplication : Application() {
    companion object {
        private var app: DyApplication? = null
        var currentActivity: Activity? = null

        fun getApp(): DyApplication? {
            return app
        }

        fun getContext(): Context? {
            return app?.applicationContext
        }
    }

    private var isBackGround = false
    override fun onCreate() {
        super.onCreate()
        app = this
        if (!ProcessUtils.isMainProcess()) {
            return
        }
        SugarContext.init(this)
        RxHttpManager.init(this)
        PathUtils.initPathConfig(this)
        val value: Boolean = SPManager.getValue(SPManager.AGREE_USER_PROTOCOL, false)
        if (value) {
//            ThirdSdkInit.initSdk()
            registerActivityLifecycle()
        }

    }


    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate()
    }
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            isBackGround = true
        }
    }


    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}