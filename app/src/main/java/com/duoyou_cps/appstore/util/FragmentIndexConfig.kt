package com.duoyou_cps.appstore.util

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.duoyou.develop.base.BaseFragment
import com.duoyou.develop.entity.TabHolder
import com.duoyou.develop.entity.TabInfo
import com.duoyou.develop.entity.UserInfo
import com.duoyou.develop.ui.WebViewFragment
import com.duoyou.develop.utils.AppInfoUtils
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.impi.ILoginStatusChange
import com.duoyou_cps.appstore.ui.frag.MineFragment
import com.duoyou_cps.appstore.util.httputils.APP_CLASSIFY
import com.duoyou_cps.appstore.util.httputils.APP_HOME
import com.duoyou_cps.appstore.util.httputils.HttpUrlKt


const val MAIN_TAB_HOME = 0
const val MAIN_TAB_CLASS = 1
const val MAIN_TAB_ME = 2
val mainFragmentIndexList = arrayListOf(MAIN_TAB_HOME, MAIN_TAB_CLASS, MAIN_TAB_ME)
val mainTabInfoList = initMainTabInfo()
//val mainTabHolderList by lazy { initH }

fun initMainTabFrag(): ArrayList<BaseFragment> {
    logi("initMainTabFrag")
    val list = ArrayList<BaseFragment>()

    for (position in 0 until mainFragmentIndexList.size) {
        list.add(initFragment(position))
    }
    return list
}


private fun initFragment(position: Int): BaseFragment {
//    val s = if (LoginImplManager.getLoginStatus()) {
//        "?token=${UserInfo.getInstance().accessToken}&channel=${AppInfoUtils.getChannel()}"
//    } else {
//        ""
//    }
    return when (mainFragmentIndexList[position]) {
        MAIN_TAB_HOME -> WebViewFragment.newInstance(HttpUrlKt.getH5HttpUrl(APP_HOME))
        MAIN_TAB_CLASS -> WebViewFragment.newInstance(HttpUrlKt.getH5HttpUrl(APP_CLASSIFY))
        else -> MineFragment.newInstance()
    }
}

fun initMainTabInfo(): ArrayList<TabInfo> {
    val tabHolder = ArrayList<TabInfo>()
    for (i in 0 until mainFragmentIndexList.size) {
        tabHolder.add(getTabInfo(i))
    }
    return tabHolder
}

private fun getTabInfo(position: Int): TabInfo {
    val num = mainFragmentIndexList[position]
    val tabInfo = TabInfo()
    tabInfo.num = num
    tabInfo.name = getMainTabTitle(position)
    when (num) {
        MAIN_TAB_HOME -> {
            // 充电
            tabInfo.normalIcon = R.drawable.icon_tab_home_nor
            tabInfo.selectedIcon = R.drawable.icon_tab_home_sel
        }
        MAIN_TAB_CLASS -> {
            // 小游戏
            tabInfo.normalIcon = R.drawable.icon_tab_classify_nor
            tabInfo.selectedIcon = R.drawable.icon_tab_classify_sel
        }
        else -> {
            // 我的
            tabInfo.normalIcon = R.drawable.icon_tab_mine_nor
            tabInfo.selectedIcon = R.drawable.icon_tab_mine_sel
        }
    }
    return tabInfo
}

private fun getMainTabTitle(position: Int): String? {
    return when (mainFragmentIndexList[position]) {
        MAIN_TAB_HOME -> "首页"
        MAIN_TAB_CLASS -> "分类"
        else -> "我的"
    }
}

fun createBottomTabLayout(
    context: Activity,
    bottomTabLayout: LinearLayout,
    tabInfoList: List<TabInfo>,
): MutableList<TabHolder> {

    val mutableListOf = mutableListOf<TabHolder>()
    val size = tabInfoList.size
    for (i in 0 until size) {
        val tabInfo: TabInfo = tabInfoList[i]
        val tabHolder = TabHolder()
        val view: View = context.layoutInflater.inflate(
            R.layout.lay_main_tab_bottom_item,
            bottomTabLayout,
            false
        )
        tabHolder.tabIconIv = view.findViewById(R.id.tab_icon_iv)
        tabHolder.tabNameTv = view.findViewById(R.id.tab_name_tv)
        mutableListOf.add(tabHolder)
        tabHolder.tabIconIv.setImageResource(tabInfo.normalIcon)
        tabHolder.tabNameTv.text = tabInfo.name
        view.tag = i
        bottomTabLayout.addView(view)
    }
    return mutableListOf
}