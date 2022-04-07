package com.duoyou_cps.appstore.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

/**
 * User: ljx
 * Date: 2020/6/2
 * Time: 11:45
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected lateinit var mBinding: T

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        initExtraView()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.onViewCreated(savedInstanceState)
    }

    abstract fun initExtraView()

    open fun T.onViewCreated(savedInstanceState: Bundle?) {

    }


}