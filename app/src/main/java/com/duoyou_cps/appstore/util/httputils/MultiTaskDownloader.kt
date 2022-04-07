package com.duoyou_cps.appstore.util.httputils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.FileUtils
import com.duoyou_cps.appstore.db.DownloadTask
import com.duoyou_cps.appstore.util.showToast
import com.duoyou_cps.appstore.util.Preferences
import com.rxjava.rxlife.RxLife
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okio.ByteString.Companion.encodeUtf8
import rxhttp.wrapper.param.RxHttp
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * User: ljx
 * Date: 2020/7/12
 * Time: 18:00
 */
object MultiTaskDownloader {

    const val IDLE = 0             //未开始，闲置状态
    const val WAITING = 1          //等待中状态
    const val DOWNLOADING = 2      //下载中
    const val PAUSED = 3           //已暂停
    const val COMPLETED = 4        //已完成
    const val FAIL = 5             //下载失败
    const val CANCEL = 6           //取消状态，等待时被取消

    private const val MAX_TASK_COUNT = 3   //最大并发数

    @JvmStatic
    val allLiveTask = MutableLiveData<ArrayList<DownloadTask>>() //所有下载任务
    private val waitTask = LinkedList<DownloadTask>() //等待下载的任务
    private val downloadingTask = LinkedList<DownloadTask>() //下载中的任务

    private val lengthMap = HashMap<String, Long>()
    private var singleTaskListener: MutableMap<Long, SingleTaskListener> = mutableMapOf()

    fun setSingleTaskListener(task: DownloadTask, lis: SingleTaskListener) {
        singleTaskListener[task.id] = lis
    }

    fun cancelSingleTaskListener(long: Long) {
        singleTaskListener.remove(long)
    }

    private fun callTaskListener(task: DownloadTask) {
        singleTaskListener[task.id]?.updateTaskProgress(task)
    }


    @JvmStatic
    fun addTasks(tasks: List<DownloadTask>) {
        val allTaskList = getAllTask()
        tasks.forEach {
            if (!allTaskList.contains(it)) {
                val md5Key = it.url.encodeUtf8().md5().hex()
                val length = Preferences.getValue(md5Key, -1L)
                if (length != -1L) {
                    it.totalSize = length
                    it.currentSize = File(it.localPath).length()
                    it.progress = (it.currentSize * 100 / it.totalSize).toInt()
                    lengthMap[it.url] = length

                    if (it.currentSize > 0) {
                        it.state = PAUSED
                    }
                    if (it.totalSize != 0L && it.currentSize >= it.totalSize) {
                        //如果当前size等于总size，则任务文件下载完成，注意: 这个判断不是100%准确，最好能对文件做md5校验
                        it.state = COMPLETED
                    }
                }
                it.save()
                allTaskList.add(it)
            }
        }
        allLiveTask.value = allTaskList
    }

    //开始下载所有任务
    @JvmStatic
    fun startAllDownloadTask() {
        val allTaskList = getAllTask()
        allTaskList.forEach {
            if (it.state != COMPLETED && it.state != DOWNLOADING) {
                download(it)
            }
        }
    }

    @JvmStatic
    fun download(task: DownloadTask) {
        if (downloadingTask.size >= MAX_TASK_COUNT) {
            task.state = WAITING
            waitTask.offer(task)
            callTaskListener(task)
//            singleTaskListener(task)
            task.save()
            return
        }
        val disposable = RxHttp.get(task.url)
            .asAppendDownload(task.localPath, AndroidSchedulers.mainThread()) {
                //下载进度回调,0-100，仅在进度有更新时才会回调
                task.progress = it.progress        //当前进度 0-100
                task.currentSize = it.currentSize  //当前已下载的字节大小
                task.totalSize = it.totalSize      //要下载的总字节大小
                updateTask()
                val key = task.url
                val length = lengthMap[key]
                if (length != task.totalSize) {
                    lengthMap[key] = task.totalSize
                    saveTotalSize()
                }
                task.save()
                callTaskListener(task)

            }
            .doFinally {
                task.save()
                callTaskListener(task)
                updateTask()
                //不管任务成功还是失败，如果还有在等待的任务，都开启下一个任务
                downloadingTask.remove(task)
                waitTask.poll()?.let { download(it) }
            }
            .subscribe({
                showToast("${task.fileName}已下载完成,点击安装")
                task.state = COMPLETED
            }, {
                it.show()
//                Tip.show("下载失败")
                task.state = FAIL
            })
        task.state = DOWNLOADING
        task.disposable = disposable
        downloadingTask.add(task)
    }


    private fun saveTotalSize() {
        Log.e("LJX", "saveTotalSize=${lengthMap.size}")
        val editor = Preferences.getEditor()
        for ((key, value) in lengthMap) {
            val md5Key = key.encodeUtf8().md5().hex()
            editor.putLong(md5Key, value)
        }
        editor.commit()
    }


    //关闭所有任务
    @JvmStatic
    fun cancelAllTask() {
        var iterator = waitTask.iterator()
        while (iterator.hasNext()) {
            val task = iterator.next()
            task.state = CANCEL
            iterator.remove()
            task.save()
        }

        iterator = downloadingTask.iterator()
        while (iterator.hasNext()) {
            val task = iterator.next()
            iterator.remove()
            val disposable = task.disposable
            RxLife.dispose(disposable)
            task.state = CANCEL
            task.save()
        }
        updateTask()
    }

    @JvmStatic
    fun deleteTask(task: DownloadTask, deleteSQL: Boolean) {
        if (downloadingTask.contains(task)) {
            downloadingTask.remove(task)
            RxLife.dispose(task.disposable)
        }
        if (waitTask.contains(task))
            waitTask.remove(task)

        val allTaskList = getAllTask()
        if (allTaskList.contains(task)) {
            allTaskList.remove(task)
        }
        allLiveTask.value = allTaskList
        updateTask()
        if (deleteSQL) {
            task.delete()
            FileUtils.delete(task.localPath)
        }
    }


    //等待中->取消下载
    @JvmStatic
    fun removeWaitTask(task: DownloadTask) {
        waitTask.remove(task)
        task.state = CANCEL
        task.save()
        updateTask()
    }

    //暂停下载
    @JvmStatic
    fun pauseTask(task: DownloadTask) {
        val disposable = task.disposable
        if (!RxLife.isDisposed(disposable)) {
            disposable.dispose()
            task.state = PAUSED
            task.save()
            updateTask()
        }
    }

    @JvmStatic
    fun haveTaskExecuting(): Boolean {
        return waitTask.size > 0 || downloadingTask.size > 0
    }

    //发送通知，更新UI
    private fun updateTask() {
        val allTask = getAllTask()
        allLiveTask.value = allTask

    }

    fun getAllTask(): ArrayList<DownloadTask> {
        return allLiveTask.value ?: ArrayList()
    }

    interface SingleTaskListener {
        fun updateTaskProgress(task: DownloadTask);
    }

}