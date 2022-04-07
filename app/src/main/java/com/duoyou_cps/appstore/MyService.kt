package com.duoyou_cps.appstore

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

open class MyService : Service() {

    override fun onBind(intent: Intent): IBinder {

        Log.i("Life_recycler","onBind")
        return MyBind()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        Log.i("Life_recycler","onStart")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Life_recycler","onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.i("Life_recycler","onRebind")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i("Life_recycler","onUnbind")
        return super.onUnbind(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i("Life_recycler","onDestroy")
    }

    open fun operateSer(){

    }


     inner class MyBind : Binder(){

        fun changeServiceName(){
            operateSer()
        }
    }

}