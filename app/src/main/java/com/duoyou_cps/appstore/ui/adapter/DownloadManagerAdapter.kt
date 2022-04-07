package com.duoyou_cps.appstore.ui.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.duoyou.develop.utils.CommonUtils
import com.duoyou.develop.view.recyclerview.BaseSimpleRecyclerAdapter
import com.duoyou.develop.view.recyclerview.ViewHolder
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.db.DownloadTask
import com.duoyou_cps.appstore.ui.act.DOWNLOAD_STATUS_INSTALLED
import com.duoyou_cps.appstore.util.httputils.MultiTaskDownloader
import java.io.File


class DownloadManagerAdapter(val type: Int) : BaseSimpleRecyclerAdapter<DownloadTask>() {
    /**
     *  0,下载中，1，已安装
     */
    override fun getLayoutId(): Int {
        return R.layout.lay_item_download_list
    }

    override fun convert(holder: ViewHolder, t: DownloadTask, position: Int) {
        holder.setText(R.id.tv_app_name, t.fileName)
        val view = holder.getView<TextView>(R.id.tv_operate)
        if (t.state == MultiTaskDownloader.COMPLETED && !CommonUtils.isAppInstalled(t.packageName)) {
            view.setBackgroundResource(R.drawable.shape_down_complete)
            view.setTextColor(ContextCompat.getColor(holder.context, R.color.white))
        } else {
            view.setBackgroundResource(R.drawable.shape_down_loading)
            view.setTextColor(ContextCompat.getColor(holder.context, R.color.theme_color))
        }
        holder.setVisible(R.id.iv_delete, t.showDelete)
        holder.setOnClickListener(R.id.iv_delete, position, onItemContentClickListener)
        holder.setOnClickListener(R.id.tv_operate, position, onItemContentClickListener)
        holder.setText(
            R.id.tv_operate, when (t.state) {
                MultiTaskDownloader.IDLE -> "开始"
                MultiTaskDownloader.WAITING -> "取消"
                MultiTaskDownloader.DOWNLOADING -> "${t.progress}%"
                MultiTaskDownloader.PAUSED -> "继续下载"
                MultiTaskDownloader.COMPLETED -> {
                    if (type == DOWNLOAD_STATUS_INSTALLED) {
                        val appInstalled = CommonUtils.isAppInstalled(t.packageName)
                        if (appInstalled) "打开" else {
                            val file = File(t.localPath)
                            if (file.exists())
                                "安装"
                            else "重新下载"
                        }
                    } else {
                        val file = File(t.localPath)
                        if (file.exists())
                            "安装"
                        else "重新下载"
                    }
                }
                MultiTaskDownloader.FAIL -> "重新下载"
                MultiTaskDownloader.CANCEL -> "继续下载"
                else -> "开始下载"
            }
        )
    }
}