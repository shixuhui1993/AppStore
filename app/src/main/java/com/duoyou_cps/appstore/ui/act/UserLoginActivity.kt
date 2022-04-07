package com.duoyou_cps.appstore.ui.act

import HttpCallback
import android.text.InputType
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.duoyou.develop.base.BaseCompatActivity
import com.duoyou.develop.entity.UserInfo
import com.duoyou.develop.view.LoadingDialog
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.api.sendVerifyCode
import com.duoyou_cps.appstore.entity.CommonEntity
import com.duoyou_cps.appstore.entity.LoginEntity
import com.duoyou_cps.appstore.util.CodeUtil
import com.duoyou_cps.appstore.util.LoginImplManager
import com.duoyou_cps.appstore.util.httputils.*
import com.duoyou_cps.appstore.util.logi
import com.duoyou_cps.appstore.util.showToast
import com.hjq.shape.view.ShapeEditText
import com.hjq.shape.view.ShapeTextView
import httpRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import rxhttp.wrapper.param.RxHttp

const val LOGIN_TYPE_ACCOUNT = 0;
const val LOGIN_TYPE_MOBILE = 1;

class UserLoginActivity : BaseCompatActivity(), View.OnClickListener {
    private val mTvLogin by lazy { findViewById<ShapeTextView>(R.id.tv_login) }
    private val mTvCode by lazy { findViewById<ShapeTextView>(R.id.tv_code) }
    private val mEtPsw by lazy { findViewById<ShapeEditText>(R.id.et_psw) }
    private val mEtAccount by lazy { findViewById<ShapeEditText>(R.id.et_account) }
    private val mTvLoginTypeMobile by lazy { findViewById<TextView>(R.id.tv_login_type_mobile) }
    private val mTvLoginTypeAccount by lazy { findViewById<TextView>(R.id.tv_login_type_account) }
    private var mCountDownUtil: CodeUtil? = null

    override fun getLayoutId() = R.layout.act_user_login
    private var currentType = LOGIN_TYPE_ACCOUNT


    override fun initView() {
    }

    override fun initListener() {
        mTvLoginTypeMobile.setOnClickListener(this)
        mTvLoginTypeAccount.setOnClickListener(this)
        mTvCode.setOnClickListener(this)
        mTvLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_login_type_account -> {
                if (currentType != LOGIN_TYPE_ACCOUNT) {
                    // 选择账号登录
                    mTvLoginTypeAccount.setBackgroundResource(R.drawable.icon_login_type_account)
                    mTvLoginTypeMobile.background = null
                    mTvLoginTypeMobile.text = "短信登录"
                    mTvLoginTypeAccount.text = ""
                    mEtAccount.inputType = InputType.TYPE_CLASS_TEXT
                    mEtPsw.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    mEtAccount.hint = "请输入多游账号"
                    mEtAccount.setText("")
                    mEtPsw.setText("")
                    mEtPsw.hint = "请输入密码"
                    mEtPsw.maxEms = 30
                    mTvLoginTypeMobile.maxEms = 30
                    mTvCode.visibility = View.GONE
                }
                currentType = LOGIN_TYPE_ACCOUNT
            }
            R.id.tv_login_type_mobile -> {
                if (currentType != LOGIN_TYPE_MOBILE) {
                    mTvLoginTypeMobile.setBackgroundResource(R.drawable.icon_login_type_mobile)
                    mTvLoginTypeAccount.background = null
                    mTvLoginTypeMobile.text = ""
                    mTvLoginTypeMobile.maxEms = 11
                    mTvLoginTypeAccount.text = "账号登录"
                    mEtAccount.hint = "请输入手机号"
                    mEtAccount.setText("")
                    mEtPsw.setText("")
                    mEtAccount.inputType = InputType.TYPE_CLASS_PHONE
                    mEtPsw.inputType = InputType.TYPE_CLASS_NUMBER
                    mEtPsw.hint = "请输入验证码"
                    mTvCode.visibility = View.VISIBLE
                    mEtPsw.maxEms = 6
                }
                currentType = LOGIN_TYPE_MOBILE

            }
            R.id.tv_code -> {
                val phone = mEtAccount.text.toString()
                if (!RegexUtils.isMobileExact(phone)) {
                    showToast("手机号格式不正确")
                    return
                }
                val mutableMapOf = mutableMapOf<String, Any?>()
                mutableMapOf["mobile"] = phone
                mutableMapOf["scene"] = "smsLogin"
                sendVerifyCode(
                    mutableMapOf,
                    CommonEntity::class.java,
                    object : HttpCallback<CommonEntity>() {
                        override fun onSuccess(data: CommonEntity?) {
                            showToast("验证码发送成功")
                            mCountDownUtil?.startCountDown() ?: run {
                                mCountDownUtil = CodeUtil(mTvCode, 60)
                                mCountDownUtil?.startCountDown()
                            }
                        }
                    })
            }
            R.id.tv_login -> {
                val userAccount = mEtAccount.text.toString()
                val psw = mEtPsw.text.toString()
                val mutableMapOf = mutableMapOf<String, Any?>()
                if (currentType == LOGIN_TYPE_ACCOUNT) {
                    mutableMapOf["account"] = userAccount
                    mutableMapOf["password"] = psw
                    if (StringUtils.isEmpty(userAccount.trim())) {
                        showToast("账号输入不合法")
                        return
                    }
                    if (StringUtils.isEmpty(psw.trim())) {
                        showToast("密码输入不合法")
                        return
                    }
                } else {
                    mutableMapOf["mobile"] = userAccount
                    mutableMapOf["login_captcha"] = psw
                    mutableMapOf["scene"] = "smsLogin"
                    if (!RegexUtils.isMobileExact(userAccount)) {
                        showToast("手机号格式不正确")
                        return
                    }
                    if (StringUtils.isEmpty(psw)) {
                        showToast("验证码格式不正确")
                        return
                    }
                }
                val loadingDialog = LoadingDialog(activity, "正在登录")
                loadingDialog.show()


                httpRequest(if (currentType == LOGIN_TYPE_ACCOUNT) LOGIN_ACCOUNT else LOGIN_PHONE,
                    mutableMapOf,
                    LoginEntity::class.java,
                    object : HttpCallback<LoginEntity>() {
                        override fun onSuccess(data: LoginEntity?) {
                            UserInfo.getInstance().setAccess_token(data?.access_token)
                            UserInfo.getInstance().updateUserInfo(UserInfo.getInstance())
                            LoginImplManager.login()
                            finish()
                        }

                        override fun onFailed(code: Int, msg: String?) {
                            super.onFailed(code, msg)
                            loadingDialog.dismiss()
                        }
                    })
            }
        }
    }

    override fun onDestroy() {
        mCountDownUtil?.onDestroy()
        super.onDestroy()
    }



}