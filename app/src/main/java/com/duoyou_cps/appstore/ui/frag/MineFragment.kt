package com.duoyou_cps.appstore.ui.frag

import HttpCallback
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.duoyou.develop.application.DyApplication
import com.duoyou.develop.base.AbsListFragment
import com.duoyou.develop.entity.UserInfo
import com.duoyou.develop.utils.glide.GlideUtils
import com.duoyou.develop.view.recyclerview.ViewHolder
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.entity.MineInfoEntity
import com.duoyou_cps.appstore.entity.OptionEntity
import com.duoyou_cps.appstore.entity.ServiceInfo
import com.duoyou_cps.appstore.impi.ILoginStatusChange
import com.duoyou_cps.appstore.ui.act.*
import com.duoyou_cps.appstore.ui.adapter.OptionAdapter
import com.duoyou_cps.appstore.util.*
import com.duoyou_cps.appstore.util.httputils.SERVICE_INFO
import com.duoyou_cps.appstore.util.httputils.USER_MEMBER_INFO
import httpRequest

class MineFragment : AbsListFragment<OptionEntity>(), ILoginStatusChange {
    private val mTvDedicatedBalance by lazy { findViewById<TextView>(R.id.tv_dedicated_balance) }
    private val mTvPlatformBalance by lazy { findViewById<TextView>(R.id.tv_platform_balance) }
    private val mTvMinePhone by lazy { findViewById<TextView>(R.id.tv_mine_phone) }
    private val mTvMineUserId by lazy { findViewById<TextView>(R.id.tv_mine_user_id) }
    private val mTvUserNickname by lazy { findViewById<TextView>(R.id.tv_user_nickname) }
    private val mIvMinePortrait by lazy { findViewById<ImageView>(R.id.iv_mine_portrait) }

    companion object {
        fun newInstance(): MineFragment {
            val args = Bundle()
            val fragment = MineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.frg_tab_mine
    override fun initAdapter() = OptionAdapter()
    override fun initView() {
        setStatusBar()
        LoginImplManager.addListener(this)
        updateUserInfo()
    }

    override fun initData() {
        replaceAll(ListOptionUtil.initSettingOption())
        loadUserInfo()
    }

    private fun loadUserInfo() {
        httpRequest(
            USER_MEMBER_INFO,
            mutableMapOf(),
            MineInfoEntity::class.java, true,
            object : HttpCallback<MineInfoEntity>() {
                override fun onSuccess(data: MineInfoEntity?) {
                    data?.let {
                        val instance = UserInfo.getInstance()
                        instance.uid = it.uid
                        instance.headerpic = it.avatar
                        instance.mobile = it.mobile
                        instance.nickname = it.nickname
                        instance.updateUserInfo(instance)
                        updateUserInfo()
                        it.special_coins
                        updateCoinInfo(
                            getAmountString(it.platform_coins),
                            getAmountString(it.special_coins)
                        )
                    } ?: showToast("获取用户信息错误")
                }
            })
    }

    private fun updateCoinInfo(platform: String, special: String) {
        mTvPlatformBalance.text = platform
        mTvDedicatedBalance.text = special
    }

    private fun updateUserInfo() {
        val instance = UserInfo.getInstance()
        GlideUtils.loadCircleImageView(activity, instance.headerpic, mIvMinePortrait)
        mTvUserNickname.text =
            if (StringUtils.isEmpty(instance.nickname)) "未设置用户名" else instance.nickname
        instance.mobile?.let {
            mTvMinePhone.text = it
        } ?: let { mTvMinePhone.text = "绑定手机" }
        mTvMineUserId.text = "ID:${instance.uid}"
    }


    override fun onItemClick(viewHolder: ViewHolder?, position: Int) {
        if (!UserInfo.getInstance().isLogin) {
            startActivity(Intent(activity, UserLoginActivity::class.java))
            return
        }
        when (getDatas()[position].id) {
            0 -> startActivity(Intent(context, ChangePswActivity::class.java))
            5 -> startActivity(Intent(context, AboutMeActivity::class.java))
            1 -> {
                if (!UserInfo.getInstance().isBindMobile) {
                    showToast("您还未绑定手机号")
                    return
                }
                val intent = Intent(context, BindOrUnBindMobileActivity::class.java)
                intent.putExtra("pageType", 1)
                startActivity(intent)
            }
            2 -> {

                startActivity(Intent(context, AppDownloadManaActivity::class.java))
                // 下载管理
            }
            3 -> {
//                addDownloadTask()
//                showToast("初始化下载")
            }
            4 -> {
                httpRequest(
                    SERVICE_INFO,
                    mutableMapOf(),
                    ServiceInfo::class.java, true,
                    object : HttpCallback<ServiceInfo>() {
                        override fun onSuccess(data: ServiceInfo?) {
                            val intent = Intent(activity, ServiceInfoActivity::class.java)
                            data?.let {
                                intent.putExtra("customer_type", it.customer_type)
                                intent.putExtra("customer_qr", it.customer_qr)
                                intent.putExtra("customer_no", it.customer_no)
                                startActivity(intent)
                            } ?: showToast("信息获取出错")
                        }
                    })
            }
        }
    }

    override fun initListener() {
        mTvMinePhone.setOnClickListener {
            if (!UserInfo.getInstance().isBindMobile) {
                val intent = Intent(context, BindOrUnBindMobileActivity::class.java)
                intent.putExtra("pageType", 0)
                startActivity(intent)
            }
        }

    }

    override fun onDestroyView() {
        LoginImplManager.removeListener(this)
        super.onDestroyView()
    }

    override fun requestData(isRefresh: Boolean) {
    }

    override fun login() {
        loadUserInfo()
    }

    override fun logout() {
        mTvDedicatedBalance.text = "0"
        mTvPlatformBalance.text = "0"
        updateUserInfo()
    }


}