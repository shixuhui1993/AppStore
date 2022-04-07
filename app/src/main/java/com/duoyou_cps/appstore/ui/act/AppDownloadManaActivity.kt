package com.duoyou_cps.appstore.ui.act

import android.content.BroadcastReceiver
import android.os.Environment
import android.os.StatFs
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.duoyou.develop.adapter.CurrencyFragmentAdapter
import com.duoyou.develop.base.BaseCompatActivity
import com.duoyou.develop.base.BaseFragment
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.db.DownloadTask
import com.duoyou_cps.appstore.receiver.AppInstallListener
import com.duoyou_cps.appstore.receiver.regisBroadcastReceiver
import com.duoyou_cps.appstore.ui.frag.DownloadManagerFragment
import com.duoyou_cps.appstore.util.getAmountString
import com.flyco.tablayout.SlidingTabLayout

const val DOWNLOAD_STATUS_INSTALLED = 1
const val DOWNLOAD_STATUS_LOADING = 0

class AppDownloadManaActivity : BaseCompatActivity(), AppInstallListener {
    override fun getLayoutId() = R.layout.act_app_download_mana
    private val mTabLayout by lazy { findViewById<SlidingTabLayout>(R.id.tab_layout) }
    private val mViewPager by lazy { findViewById<ViewPager>(R.id.view_pager) }
    private val mTvEdit by lazy { findViewById<TextView>(R.id.tv_edit) }
    private val mTvAlive by lazy { findViewById<TextView>(R.id.tv_total_alive) }
    private val mFragments by lazy { arrayListOf<BaseFragment>() }
    private var mCurrentIsEdit = false

    override fun initView() {
        mFragments.add(DownloadManagerFragment.newInstance("下载中", DOWNLOAD_STATUS_LOADING))
        mFragments.add(DownloadManagerFragment.newInstance("已安装", DOWNLOAD_STATUS_INSTALLED))
        val currencyFragmentAdapter =
            CurrencyFragmentAdapter(
                supportFragmentManager,
                mFragments
            )
        mViewPager.adapter = currencyFragmentAdapter
        mTabLayout.setViewPager(mViewPager)
        mTvAlive.text = getAvailableInternalMemorySize()
    }

    override fun initListener() {
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mTvEdit.visibility = if (position == 0) View.VISIBLE else View.GONE
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        mTvEdit.setOnClickListener {
            mCurrentIsEdit = !mCurrentIsEdit
            val downloadManagerFragment = mFragments[0] as DownloadManagerFragment
            downloadManagerFragment.editDownloadTask(mCurrentIsEdit)
            mTvEdit.text = if (mCurrentIsEdit) "完成" else "编辑"
        }
    }

    private var lastReceiver: BroadcastReceiver? = null
    fun regisBroadCast(task: DownloadTask) {
        lastReceiver?.let {
            unregisterReceiver(it)
        }
        lastReceiver = regisBroadcastReceiver(this, task, this)
    }

    private fun getAvailableInternalMemorySize(): String {
        val root = Environment.getRootDirectory()

        val sf = StatFs(root.path)
        val blockCountLong = sf.blockCountLong
        val blockSize = sf.blockSizeLong
        val availCount = sf.availableBlocksLong

        val totalBlock = blockCountLong * blockSize * 1.0 / 1024 / 1024 / 1024
        val totalAvail = availCount * blockSize * 1.0 / 1024 / 1024 / 1024
        return "总：${getAmountString(totalBlock)}GB/可用${getAmountString(totalAvail)}GB"
    }

    override fun appInstallSuccess(task: DownloadTask) {
        lastReceiver = null
        for (frag in mFragments) {
            val downloadManagerFragment = frag as DownloadManagerFragment
            downloadManagerFragment.installSuccess(task)
        }
    }

}