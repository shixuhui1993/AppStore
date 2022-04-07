package com.duoyou_cps.appstore.ui.act

import android.content.Intent
import com.duoyou.develop.base.BaseCompatActivity
import com.duoyou_cps.appstore.MainActivity
import com.duoyou_cps.appstore.R

class SplashActivity : BaseCompatActivity() {
    override fun getLayoutId() = R.layout.activity_splash

    override fun initView() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}