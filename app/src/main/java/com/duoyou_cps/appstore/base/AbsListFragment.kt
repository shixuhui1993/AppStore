package com.duoyou_cps.appstore.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.duoyou.develop.view.recyclerview.BaseSimpleRecyclerAdapter
import com.duoyou.develop.view.recyclerview.wrapper.EmptyWrapper
import com.duoyou_cps.appstore.R
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

abstract class AbsListFragment<T : ViewDataBinding,E>:BaseFragment<T>() {
    protected val mRecyclerView: RecyclerView? by lazy { findViewById(R.id.recycler_view) }
    protected val mSmartRefreshLayout: SmartRefreshLayout? by lazy {
        findViewById(
            R.id.smart_refresh_layout
        )
    }
    protected val emptyWrapper by lazy { EmptyWrapper(initAdapter()) }

    abstract fun initAdapter(): BaseSimpleRecyclerAdapter<E>

     override fun initExtraView() {

        mRecyclerView?.adapter = emptyWrapper
        val arrayListOf = arrayListOf<E>()
        emptyWrapper.itemManager.replaceAllItem(arrayListOf)
        initSmartRefreshLayout()
        getEmptyView()?.let {
            emptyWrapper.setEmptyView(it)
        }
    }

    open fun getEmptyView(): View? {
        return null
    }
    abstract fun requestData(isRefresh: Boolean)

    private fun initSmartRefreshLayout() {
        mSmartRefreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                requestData(false)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                requestData(true)
            }
        })

    }
    open fun <N : View?> findViewById(rid: Int): N? {
        return mBinding.root.findViewById(rid)
    }

}