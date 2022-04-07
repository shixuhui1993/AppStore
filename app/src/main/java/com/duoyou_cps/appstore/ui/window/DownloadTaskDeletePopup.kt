package com.duoyou_cps.appstore.ui.window

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.duoyou_cps.appstore.R

class DownloadTaskDeletePopup(
    private val mContext: Context,
    private val targetView: View,
    private val listener: PopupOperateListener
) :
    PopupWindow() {
    private val containView by lazy { contentView.findViewById<ConstraintLayout>(R.id.cl_contain) }
    private val mTvTip by lazy { contentView.findViewById<TextView>(R.id.tv_delete_task_tip) }
    private val mTvSure by lazy { contentView.findViewById<TextView>(R.id.tv_sure) }
    private val mTvCancel by lazy { contentView.findViewById<TextView>(R.id.tv_cancel) }

    init {
        initView()
    }

    private fun initView() {

        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_window_delete_task, null)
        contentView = view

        //设置SelectPicPopupWindow弹出窗体的高
        width = ConstraintLayout.LayoutParams.MATCH_PARENT
        height = ConstraintLayout.LayoutParams.MATCH_PARENT
        //设置SelectPicPopupWindow弹出窗体可点击
        isFocusable = true
        //设置SelectPicPopupWindow弹出窗体动画效果
//        animationStyle = R.style.pupup_show_anim
        //实例化一个ColorDrawable颜色为半透明
        //设置SelectPicPopupWindow弹出窗体的背景
        isClippingEnabled = false
        isOutsideTouchable = true
        this.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B2000000")))
        setListener()

//        containView.animation =
//            AnimationUtils.loadAnimation(mContext, R.anim.popup_anim_bottom_to_top)
    }

    private fun updatePage(fileName: String) {
        mTvTip.text =
            String.format(mContext.getString(R.string.string_delete_download_task_tip), fileName)
    }

    private fun setListener() {
        mTvCancel.setOnClickListener {
            dismiss()
        }
        mTvSure.setOnClickListener {
            listener.sure()
            dismiss()
        }
    }

    fun show() {
        showAtLocation(targetView, Gravity.BOTTOM, 0, 0)
    }

    companion object {
        fun showPopup(
            mContext: Context?,
            targetView: View,
            ids: String,
            listener: PopupOperateListener
        ) {
            mContext ?: return
            val goodsRecyclerPopup = DownloadTaskDeletePopup(mContext, targetView, listener)
            goodsRecyclerPopup.updatePage(ids)
            goodsRecyclerPopup.show()
        }

    }

    interface PopupOperateListener {
        fun sure()
    }
}