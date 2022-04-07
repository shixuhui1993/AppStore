package com.duoyou_cps.appstore.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.duoyou.develop.base.BaseCompatActivity

abstract class VMBaseActivity<T : ViewDataBinding> : BaseCompatActivity() {

    protected lateinit var mBinding: T

    abstract override fun getLayoutId(): Int
    override fun setContentView() {
        mBinding = DataBindingUtil.setContentView(this, layoutId)
        setContentView(mBinding.root)
    }

    override fun initView() {

    }
    open fun loadData() {

    }



}