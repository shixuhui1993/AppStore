package com.duoyou_cps.appstore.ui.act

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.duoyou.develop.base.BaseCompatActivity
import com.duoyou.develop.utils.CommonUtils
import com.duoyou.develop.utils.glide.GlideUtils
import com.duoyou_cps.appstore.R
import com.hjq.shape.view.ShapeTextView

class ServiceInfoActivity : BaseCompatActivity() {
    private val mIvQrCode by lazy { findViewById<ImageView>(R.id.iv_qr_code) }
    private val mTvCopy by lazy { findViewById<ShapeTextView>(R.id.tv_copy) }
    private val mTvContactNum by lazy { findViewById<TextView>(R.id.tv_contact_num) }
    private val mIvPortrait by lazy { findViewById<ImageView>(R.id.iv_portrait) }
    override fun getLayoutId() = R.layout.act_service_info
    override fun title() = "联系客服"


    override fun initView() {
        findViewById<View>(R.id.title_bar_layout).background = null
        val type = intent.getIntExtra("customer_type", 1)
        mIvPortrait.setImageResource(if (type == 1) R.drawable.icon_service_qq else R.drawable.icon_service_wechat)
        mTvContactNum.text = intent.getStringExtra("customer_no")
        GlideUtils.loadImage(this, intent.getStringExtra("customer_qr"), mIvQrCode)
    }

    override fun initListener() {
        mTvCopy.setOnClickListener {
            CommonUtils.copyText(this, mTvContactNum.text.toString())
        }
    }

}