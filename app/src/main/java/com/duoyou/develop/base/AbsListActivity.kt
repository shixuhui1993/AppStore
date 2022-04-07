package com.duoyou.develop.base

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoyou.develop.view.recyclerview.BaseSimpleRecyclerAdapter
import com.duoyou.develop.view.recyclerview.ViewHolder
import com.duoyou.develop.view.recyclerview.wrapper.BaseWrapper
import com.duoyou.develop.view.recyclerview.wrapper.EmptyWrapper
import com.duoyou_cps.appstore.R
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

abstract class AbsListActivity<T> : BaseCompatActivity(),
    BaseSimpleRecyclerAdapter.OnItemClickListener {
    protected val mRecyclerView: RecyclerView? by lazy { findViewById(R.id.recycler_view) }
    protected val mSmartRefreshLayout: SmartRefreshLayout? by lazy {
        findViewById(
            R.id.smart_refresh_layout
        )
    }
    protected val emptyWrapper by lazy { EmptyWrapper(initAdapter()) }
    protected var currentPage = 1
    abstract fun initAdapter(): BaseSimpleRecyclerAdapter<T>
    override fun initExtraView() {
        mRecyclerView?.adapter = emptyWrapper
        mRecyclerView?.layoutManager = initLayoutManager()
        initSmartRefreshLayout()
        getEmptyView()?.let {
            emptyWrapper.setEmptyView(it)
        }
    }

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

    open fun getEmptyView(): View? {
        return null
    }


    abstract fun requestData(isRefresh: Boolean)
    fun setRefreshLayoutEnable(refresh: Boolean, loadMore: Boolean) {
        mSmartRefreshLayout?.setEnableRefresh(refresh)
        mSmartRefreshLayout?.setEnableLoadMore(loadMore)

    }

    fun getWrapper(): BaseWrapper<T> {
        return emptyWrapper
    }

    fun replaceAll(datas: MutableList<T>) {
        emptyWrapper.itemManager.replaceAllItem(datas)

    }

    fun addAll(datas: MutableList<T>) {
        emptyWrapper.itemManager.addAllItems(datas)
    }

    fun setOnItemClickListener() {
        emptyWrapper.setOnItemClickListener(this)
    }

    override fun onItemClick(viewHolder: ViewHolder?, position: Int) {
    }

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

}