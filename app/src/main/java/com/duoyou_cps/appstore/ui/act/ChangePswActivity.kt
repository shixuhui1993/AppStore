package com.duoyou_cps.appstore.ui.act

import HttpCallback
import android.view.WindowManager
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.duoyou.develop.base.BaseCompatActivity
import com.duoyou.develop.view.LoadingDialog
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.entity.CommonEntity
import com.duoyou_cps.appstore.util.LoginImplManager
import com.duoyou_cps.appstore.util.httputils.Tip
import com.duoyou_cps.appstore.util.httputils.USER_CHANGE_PSW
import com.duoyou_cps.appstore.util.showToast
import com.hjq.shape.view.ShapeTextView
import httpRequest

class ChangePswActivity : BaseCompatActivity() {
    private val mTvSure by lazy { findViewById<ShapeTextView>(R.id.tv_sure) }
    private val mEtConfirmNewPsw by lazy { findViewById<EditText>(R.id.et_confirm_new_psw) }
    private val mEtNewPsw by lazy { findViewById<EditText>(R.id.et_code) }
    private val mEtOldPsw by lazy { findViewById<EditText>(R.id.et_phone_num) }
    override fun getLayoutId() = R.layout.act_change_psw
    private var mOldPsw = ""
    private var mNewPsw = ""
    private var mConfirmNewPsw = ""

    override fun initView() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


    }

    override fun initListener() {
        mEtOldPsw.addTextChangedListener {
            mOldPsw = it.toString().trim()
            checkSureBtnState()
        }
        mEtNewPsw.addTextChangedListener {
            mNewPsw = it.toString().trim()
            checkSureBtnState()
        }
        mEtConfirmNewPsw.addTextChangedListener {
            mConfirmNewPsw = it.toString().trim()
            checkSureBtnState()
        }
        mTvSure.setOnClickListener {
            if (mNewPsw != mConfirmNewPsw) {
                showToast("两次密码输入不一致")
                return@setOnClickListener
            }
            changePsw()

        }
    }

    private fun changePsw() {
        val show = LoadingDialog.show(this, "正在处理")
        val mutableMapOf = mutableMapOf<String, Any?>()
        mutableMapOf["old_pass"] = mOldPsw
        mutableMapOf["new_pass"] = mNewPsw
        mutableMapOf["new_pass_again"] = mConfirmNewPsw
        httpRequest(
            USER_CHANGE_PSW,
            mutableMapOf,
            CommonEntity::class.java,
            object : HttpCallback<CommonEntity>() {
                override fun onSuccess(data: CommonEntity?) {
                    LoginImplManager.logout()
                    showToast("密码修改成功,请重新登录")
                    finish()
                }

                override fun onFailed(code: Int, msg: String?) {
                    show.dismiss()
                    super.onFailed(code, msg)
                }
            })


    }


    private fun checkSureBtnState() {
        if (mOldPsw.length >= 6 && mNewPsw.length >= 6 && mConfirmNewPsw.length >= 6) {
            mTvSure.alpha = 1f
            mTvSure.isEnabled = true
        } else {
            mTvSure.alpha = 0.3f
            mTvSure.isEnabled = false
        }
    }


}



