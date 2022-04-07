package com.duoyou_cps.appstore

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.TimeUtils
import com.duoyou.develop.base.BaseFragment
import com.duoyou.develop.entity.UserInfo
import com.duoyou.develop.ui.WebViewFragment
import com.duoyou.develop.utils.PermissionHelper
import com.duoyou.develop.view.ToastHelper
import com.duoyou_cps.appstore.base.VMBaseActivity
import com.duoyou_cps.appstore.databinding.ActivityMainBinding
import com.duoyou_cps.appstore.impi.ILoginStatusChange
import com.duoyou_cps.appstore.ui.act.UserLoginActivity
import com.duoyou_cps.appstore.util.*

class MainActivity : VMBaseActivity<ActivityMainBinding>(), ILoginStatusChange {
    private val tabHolderList by lazy {
        createBottomTabLayout(
            this,
            mBinding.bottomTabLayout,
            mainTabInfoList,
        )
    }
    private val mainFragmentList by lazy { initMainTabFrag() }

    override fun getLayoutId() = R.layout.activity_main
    override fun initExtraView() {

//        val b = PermissionHelper.hasPermission(activity)
    }

    override fun initView() {
        logi("initView")
        initBottomTab()
        changeBottomTab(0)
        LoginImplManager.addListener(this)
    }

    private fun initBottomTab() {
        mBinding.bottomTabLayout.removeAllViews()
//        val childCount: Int = mBinding.bottomTabLayout.childCount
//        var tabHolderList1 = tabHolderList
        for (i in 0 until tabHolderList.size) {
            val childAt: View = mBinding.bottomTabLayout.getChildAt(i)
            childAt.setOnClickListener { view ->
                val position = view.tag as Int
//                if (position != 0) {
//                    EventBusUtils.post(OperateMusic(false))
//                }
//                val eventId = when (position) {
//                    0 -> "navigation_homepage"
//                    1 -> "navigation_cabinet"
//                    2 -> "navigation_shop"
//                    else -> "navigation_my"
//                }
//                ThinkingDataEvent.eventTrack(eventId)
                changeBottomTab(position)
            }
        }
    }

    private var currentPosition = -1
    private fun changeBottomTab(position: Int) {
        if (position == currentPosition) {
            return
        }
        if (position == mainTabInfoList.size - 1) {
            if (!UserInfo.getInstance().isLogin) {
                startActivity(Intent(activity, UserLoginActivity::class.java))
                return
            }
        }
        val fragment: BaseFragment = mainFragmentList[position]
        switchFragment(fragment)

//        if (fragment is WebViewFragment) {
//            fragment.refresh()
//        }

        // 选中Tab的颜色切换
        for (i in 0 until tabHolderList.size) {
            val holder = tabHolderList[i]
            val fgm = mainFragmentList[i]
            if (i == position) {
                holder.tabNameTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.homeBottomTabColorSel
                    )
                )
                fgm.onTabSelect(i)
            } else {
                holder.tabNameTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.homeBottomTabColorNor
                    )
                )
                fgm.onTabUnSelect(i)
            }
            if (i == position) {
                holder.tabIconIv.setImageResource(mainTabInfoList[i].selectedIcon)
            } else {
                holder.tabIconIv.setImageResource(mainTabInfoList[i].normalIcon)
            }
        }
        currentPosition = position
    }

    private var currentFragment: BaseFragment? = null;
    private fun switchFragment(targetFragment: BaseFragment) {
        val transaction =
            supportFragmentManager.beginTransaction()
        currentFragment?.let {
            transaction.hide(it)
        }
        if (targetFragment.isAdded) {
            transaction.show(targetFragment)
        } else {
            transaction.add(
                R.id.container_layout,
                targetFragment,
                targetFragment.javaClass.name
            )
        }
        currentFragment = targetFragment
        transaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        LoginImplManager.removeListener(this)
        LoginImplManager.clearListener()
        super.onDestroy()
    }

    override fun login() {
    }

    override fun logout() {
        changeBottomTab(0)
    }
    private var lastBackTime:Long = 0L
    override fun onBackPressed() {
        val nowMills = TimeUtils.getNowMills()
        if (nowMills - lastBackTime > 1000) {
            showToast("再按一次退出应用")
            lastBackTime = nowMills
            return
        }
        super.onBackPressed()
    }

}


val downloadUrl = arrayOf(
    "https://dl.duoyou.com/gameapk/duocaimanghe/01db1ca657aced410bfb.apk",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/uS12nZLR.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/BYGanTMW.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/Iu9hZLL8.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/DdKLk5VX.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/Byww5X8k.ts",
    "https://apk-ssl.tancdn.com/3.5.3_276/%E6%8E%A2%E6%8E%A2.apk",  //探探
    "http://update.9158.com/miaolive/Miaolive.apk",  //喵播
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/OUkREagY.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/ZJUsgPSd.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/I5ivzoXR.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/PFPXapY7.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/tJj2JTVy.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/mj2fFYjH.ts",
//    "https://hnzy4.jinhaiwzhs.com:65/20210625/mndoRF1O/2000kb/hls/MOXijkzw.ts",
)
