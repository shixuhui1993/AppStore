package com.duoyou_cps.appstore.ui.act

import HttpCallback
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.duoyou.develop.base.BaseCompatActivity
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.api.sendVerifyCode
import com.duoyou_cps.appstore.entity.CommonEntity
import com.duoyou_cps.appstore.util.CodeUtil
import com.duoyou_cps.appstore.util.httputils.USER_BIND_PHONE
import com.duoyou_cps.appstore.util.logi
import com.duoyou_cps.appstore.util.showToast
import com.hjq.shape.view.ShapeTextView
import httpRequest

class BindOrUnBindMobileActivity : BaseCompatActivity(), View.OnClickListener {
    private val mTvTitle by lazy { findViewById<TextView>(R.id.tv_title) }
    private val mTvSure by lazy { findViewById<ShapeTextView>(R.id.tv_sure) }
    private val mTvCode by lazy { findViewById<ShapeTextView>(R.id.tv_code) }
    private val mEtCode by lazy { findViewById<EditText>(R.id.et_code) }
    private val mEtPhoneNum by lazy { findViewById<EditText>(R.id.et_phone_num) }
    private val mPageType by lazy { intent.getIntExtra("pageType", 0) }
    override fun getLayoutId() = R.layout.act_bind_or_un_bind_mobile
    private var mCountDownUtil: CodeUtil? = null
    override fun initView() {
        mTvTitle.text = if (mPageType == 0) "绑定手机" else "解绑手机"
    }

    private var mPhoneNum = ""
    private var mVerifyCode = ""
    override fun initListener() {
        mTvCode.setOnClickListener(this)
        mTvSure.setOnClickListener(this)

        mEtCode.addTextChangedListener {
            mVerifyCode = it.toString()
            checkSureBtnState()
        }
        mEtPhoneNum.addTextChangedListener {
            mPhoneNum = it.toString()
            checkSureBtnState()
        }
    }

    private fun checkSureBtnState() {
        if (mVerifyCode.length == 6 && RegexUtils.isMobileExact(mPhoneNum)) {
            mTvSure.alpha = 1f
            mTvSure.isEnabled = true
        } else {
            mTvSure.alpha = 0.3f
            mTvSure.isEnabled = false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_code -> {
                val phone = mEtPhoneNum.text.toString()
                if (!RegexUtils.isMobileExact(phone)) {
                    showToast("手机号格式不正确")
                    return
                }
                val mutableMapOf = mutableMapOf<String, Any?>()
                mutableMapOf["mobile"] = phone
                mutableMapOf["scene"] = "binding"
                sendVerifyCode(
                    mutableMapOf,
                    CommonEntity::class.java,
                    object : HttpCallback<CommonEntity>() {
                        override fun onSuccess(data: CommonEntity?) {
                            logi("json__result", "ThreadName = "+Thread.currentThread())
                            showToast("验证码发送成功")
                            mCountDownUtil?.startCountDown() ?: run {
                                mCountDownUtil = CodeUtil(mTvCode, 60)
                                mCountDownUtil?.startCountDown()
                            }
                        }
                    })
            }
            R.id.tv_login -> {
                val phoneNum = mEtPhoneNum.text.toString()
                val verifyCode = mEtCode.text.toString()
                val mutableMapOf = mutableMapOf<String, Any?>()
                if (mPageType == LOGIN_TYPE_ACCOUNT) {
                    mutableMapOf["account"] = phoneNum
                    mutableMapOf["password"] = verifyCode
                    if (StringUtils.isEmpty(phoneNum.trim())) {
                        showToast("账号输入不合法")
                        return
                    }
                    if (StringUtils.isEmpty(verifyCode.trim())) {
                        showToast("密码输入不合法")
                        return
                    }
                } else {
                    mutableMapOf["mobile"] = phoneNum
                    mutableMapOf["login_captcha"] = verifyCode
                    mutableMapOf["scene"] = "binding"
                    if (!RegexUtils.isMobileExact(phoneNum)) {
                        showToast("手机号格式不正确")
                        return
                    }
                    if (StringUtils.isEmpty(verifyCode)) {
                        showToast("验证码格式不正确")
                        return
                    }
                }
                httpRequest(
                    USER_BIND_PHONE,
                    mutableMapOf,
                    CommonEntity::class.java,
                    object : HttpCallback<CommonEntity>() {
                        override fun onSuccess(data: CommonEntity?) {
                            showToast("绑定成功")
                        }
                    })
            }

        }
    }


}