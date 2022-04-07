package com.duoyou_cps.appstore.ui.act

import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.duoyou.develop.base.AbsListActivity
import com.duoyou.develop.utils.PageJumpUtils
import com.duoyou.develop.view.recyclerview.ViewHolder
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.entity.OptionEntity
import com.duoyou_cps.appstore.ui.adapter.OptionAdapter
import com.duoyou_cps.appstore.util.ListOptionUtil
import com.duoyou_cps.appstore.util.LoginImplManager
import com.hjq.shape.layout.ShapeRecyclerView
import com.hjq.shape.view.ShapeTextView
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class AboutMeActivity : AbsListActivity<OptionEntity>() {
    private val mTvLogout by lazy { findViewById<ShapeTextView>(R.id.tv_logout) }
    override fun getLayoutId() = R.layout.act_about_me

    override fun initAdapter() = OptionAdapter()

    override fun title() = "关于我们"
    override fun initView() {
        val view = mRecyclerView as ShapeRecyclerView
        view.shapeDrawableBuilder.setRadius(SizeUtils.dp2px(12f) * 1.0f)
        view.shapeDrawableBuilder.solidColor = ContextCompat.getColor(this, R.color.white)
        view.shapeDrawableBuilder.intoBackground()
        setRefreshLayoutEnable(refresh = false, loadMore = false)
    }

    override fun initData() {
        replaceAll(ListOptionUtil.initAboutMeOption(true))
    }

    override fun initListener() {
        mTvLogout.setOnClickListener {
            LoginImplManager.logout()
            finish()
        }
        setOnItemClickListener()
    }

    override fun onItemClick(viewHolder: ViewHolder?, position: Int) {
        PageJumpUtils.jumpByUrl(this, getWrapper().datas[position].url)
    }


    override fun requestData(isRefresh: Boolean) {

    }
}