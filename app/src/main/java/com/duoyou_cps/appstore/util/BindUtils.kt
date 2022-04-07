package com.duoyou_cps.appstore.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.duoyou.develop.view.recyclerview.BaseSimpleRecyclerAdapter


object BindUtils {
    @BindingAdapter("adapter")
    fun <T>setAdapterForRv(rv: RecyclerView, datas: List<T?>?) {
        val adapter  = rv.adapter as BaseSimpleRecyclerAdapter<T>?
        if (adapter != null) {
            adapter.datas = datas
        }
    }

}