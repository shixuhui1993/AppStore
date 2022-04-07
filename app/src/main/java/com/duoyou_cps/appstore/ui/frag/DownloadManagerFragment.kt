package com.duoyou_cps.appstore.ui.frag

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.*
import com.duoyou.develop.base.AbsListFragment
import com.duoyou.develop.utils.CommonUtils
import com.duoyou.develop.view.recyclerview.EmptyViewHelper
import com.duoyou_cps.appstore.R
import com.duoyou_cps.appstore.db.DownloadTask
import com.duoyou_cps.appstore.downloadUrl
import com.duoyou_cps.appstore.ui.act.AppDownloadManaActivity
import com.duoyou_cps.appstore.ui.act.DOWNLOAD_STATUS_INSTALLED
import com.duoyou_cps.appstore.ui.act.DOWNLOAD_STATUS_LOADING
import com.duoyou_cps.appstore.util.showToast
import com.duoyou_cps.appstore.ui.adapter.DownloadManagerAdapter
import com.duoyou_cps.appstore.ui.window.DownloadTaskDeletePopup
import com.duoyou_cps.appstore.util.PathUtils
import com.duoyou_cps.appstore.util.httputils.MultiTaskDownloader
import java.io.File
import java.net.URLDecoder

private const val ARG_PARAM1 = "title"
private const val ARG_PARAM2 = "status"

class DownloadManagerFragment : AbsListFragment<DownloadTask>() {
    private var type: Int = 0

    override fun getLayoutId() = R.layout.lib_common_recycler_view
    override fun initAdapter() = DownloadManagerAdapter(type)

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            DownloadManagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

    override fun getEmptyView(): View? {
        val initEmptyView = EmptyViewHelper(activity)
        initEmptyView.setOtherEmptyView("暂无下载", R.drawable.icon_empty_download)
        return initEmptyView.initEmptyView()
    }

    override fun initView() {
        arguments?.let { type = it.getInt(ARG_PARAM2) }
        mSmartRefreshLayout?.setEnableLoadMore(false)
        mSmartRefreshLayout?.setEnableRefresh(false)
    }

    override fun initData() {//https://apk-ssl.tancdn.com/3.5.3_276/探探.apk https://apk-ssl.tancdn.com/3.5.3_276/探探.apk
        val find = DownloadTask.find(
            DownloadTask::class.java,
            "installed = ?",
            type.toString()
        )
        if (type == DOWNLOAD_STATUS_LOADING) {
            val value = MultiTaskDownloader.allLiveTask.value
            value?.let {
                if (it.size == 0) {
                    MultiTaskDownloader.addTasks(find)
                }
            } ?: run { MultiTaskDownloader.addTasks(find) }
        } else {
            replaceAll(find)
        }
    }

    fun installSuccess(task: DownloadTask) {
        MultiTaskDownloader.deleteTask(task, false)
        if (type == DOWNLOAD_STATUS_INSTALLED) {
            initData()
        }
    }

    /**
     *  删除下载中任务
     */
    private fun deleteTask(task: DownloadTask) {
        MultiTaskDownloader.deleteTask(task, true)
    }

    fun editDownloadTask(isEdit: Boolean) {
        if (type != DOWNLOAD_STATUS_LOADING) {
            return
        }
        for (task in getDatas()) {
            task.showDelete = isEdit
        }
        getWrapper().itemManager.notifyDataChanged()
    }

    override fun setOnItemClickListener() {

    }

    override fun initListener() {
        if (type == DOWNLOAD_STATUS_LOADING) {

            MultiTaskDownloader.allLiveTask.observe(this) { tasks ->
                getWrapper().itemManager.replaceAllItem(tasks)
            }
        }

        getWrapper().setOnItemContentClickListener { _, position, viewId ->
            when (viewId) {
                R.id.iv_delete -> {
                    // 删除
                    showDeleteTaskDialog(position)
                }
                R.id.tv_operate -> {

                    val task = getDatas()[position]
                    // 在本地记录中是否是已安装
                    if (type == DOWNLOAD_STATUS_INSTALLED) {
                        // 判断当前是否已安装
                        if (CommonUtils.isAppInstalled(task.packageName)) {
                            // 已安装，则直接打开
                            CommonUtils.startApp(task.packageName)
                        } else {
                            // 未安装
                            val file = File(task.localPath)
                            // 判断apk是否还在
                            if (file.exists()) {
                                // apk在，则进行安装
                                (activity as AppDownloadManaActivity).regisBroadCast(task)
                                AppUtils.installApp(task.localPath)
                                return@setOnItemContentClickListener
                            }
                            showToast("安装包不存在或已被删除,请重新下载")
                            task.state = MultiTaskDownloader.FAIL
                            task.installed = DOWNLOAD_STATUS_LOADING
                            task.save()
                            MultiTaskDownloader.download(task)
                            initData()
                        }
                        return@setOnItemContentClickListener
                    }

                    val curState: Int = task.state //任务当前状态
                    if (curState == MultiTaskDownloader.IDLE //未开始->开始下载
                        || curState == MultiTaskDownloader.PAUSED //暂停下载->继续下载
                        || curState == MultiTaskDownloader.CANCEL //已取消->重新开始下载
                        || curState == MultiTaskDownloader.FAIL //下载失败->重新下载
                    ) {
                        MultiTaskDownloader.download(task)
                    } else if (curState == MultiTaskDownloader.WAITING) {       //等待中->取消下载
                        MultiTaskDownloader.removeWaitTask(task)
                    } else if (curState == MultiTaskDownloader.DOWNLOADING) {   //下载中->暂停下载
                        MultiTaskDownloader.pauseTask(task)
                    } else if (curState == MultiTaskDownloader.COMPLETED) {   //任务已完成
                        val file = File(task.localPath)
                        if (file.exists()) {
                            (activity as AppDownloadManaActivity).regisBroadCast(task)
                            AppUtils.installApp(task.localPath)
                            return@setOnItemContentClickListener
                        }
                        showToast("安装包不存在或已被删除,请重新下载")
                        task.state = MultiTaskDownloader.FAIL
                        task.save()
                        MultiTaskDownloader.download(task)
                    }
                }
            }
        }

    }

    private fun showDeleteTaskDialog(position: Int) {
        val downloadTask = getDatas()[position]

        DownloadTaskDeletePopup.showPopup(
            activity,
            rootView,
            downloadTask.fileName,
            object : DownloadTaskDeletePopup.PopupOperateListener {
                override fun sure() {
                    deleteTask(downloadTask)
                }
            })
    }

    override fun onDestroyView() {
        editDownloadTask(false)
        super.onDestroyView()
    }

    override fun requestData(isRefresh: Boolean) {


    }

}

fun addDownloadTask() {
    val arrayListOf = arrayListOf<DownloadTask>()
    for (url in downloadUrl) {
        val decode = URLDecoder.decode(url)
        val find = DownloadTask.find(DownloadTask::class.java, "download_url=?", decode)
        if (find.size != 0) {
            continue
        }
        val fileName = decode.substring(decode.lastIndexOf("/") + 1)
        val downloadTask = DownloadTask(decode)
        downloadTask.fileName = fileName
        downloadTask.packageName = "com.duoduocaihe.duoyou"
        downloadTask.localPath =
            PathUtils.getDownloadFilePath() + TimeUtils.getNowMills() + "-" + fileName

        var toJson = GsonUtils.toJson(downloadTask)

        arrayListOf.add(downloadTask)
    }
    MultiTaskDownloader.addTasks(arrayListOf)

}

