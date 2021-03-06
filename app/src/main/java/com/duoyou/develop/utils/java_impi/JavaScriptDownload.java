package com.duoyou.develop.utils.java_impi;

import static com.duoyou_cps.appstore.config.AppConfigKt.JAVA_IMPL_TARGET_DOWNLOAD;
import static com.duoyou_cps.appstore.ui.act.AppDownloadManaActivityKt.DOWNLOAD_STATUS_INSTALLED;
import static com.duoyou_cps.appstore.ui.act.AppDownloadManaActivityKt.DOWNLOAD_STATUS_LOADING;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.duoyou.develop.utils.CommonUtils;
import com.duoyou_cps.appstore.db.DownloadTask;
import com.duoyou_cps.appstore.receiver.AppInstallListener;
import com.duoyou_cps.appstore.receiver.AppInstalledReceiverKt;
import com.duoyou_cps.appstore.util.CommonFunctionKt;
import com.duoyou_cps.appstore.util.PathUtils;
import com.duoyou_cps.appstore.util.httputils.MultiTaskDownloader;
import com.tencent.smtt.sdk.WebView;

import java.net.URLDecoder;
import java.util.List;

public class JavaScriptDownload implements MultiTaskDownloader.SingleTaskListener, AppInstallListener {
    private DownloadTask downloadTask;
    private Activity mActivity;
    private final WebView mWebView;
    private BroadcastReceiver mReceiver;

    public JavaScriptDownload(Activity activity, WebView webView) {
        this.mActivity = activity;
        mWebView = webView;
    }

    @JavascriptInterface
    public void startDownloadTask(String downloadUrl, String packageName) {
        String decode = URLDecoder.decode(downloadUrl);
        String fileName = decode.substring(decode.lastIndexOf("/") + 1);
        downloadTask = new DownloadTask();
        downloadTask.setFileName(fileName);
        downloadTask.setPackageName(packageName);
        downloadTask.setLocalPath(PathUtils.getDownloadFilePath() + fileName);
        MultiTaskDownloader.INSTANCE.setSingleTaskListener(downloadTask, this);
        MultiTaskDownloader.download(downloadTask);
    }

    @JavascriptInterface
    public int checkApkInstall(String packageName) {
        return CommonUtils.isAppInstalled(packageName) ? 1 : 0;
    }


    @JavascriptInterface
    public String getDownloadInfo(String url) {
        List<DownloadTask> downloadTasks = DownloadTask.find(DownloadTask.class, "download_url=?", url);
        if (downloadTasks.size() != 0) {
            // ???????????????
            DownloadTask downloadTask = downloadTasks.get(0);
            if (downloadTask.installed == DOWNLOAD_STATUS_INSTALLED) {
                // ??????????????????
                boolean appInstalled = CommonUtils.isAppInstalled(downloadTask.getPackageName());
                if (appInstalled) {
                    // ?????????????????????????????????????????????
                    return GsonUtils.toJson(downloadTask);
                } else {
                    // ??????app????????????
                    if (FileUtils.isFileExists(downloadTask.getLocalPath())) {
                        // ?????????????????????????????????????????????
                        downloadTask.installed = DOWNLOAD_STATUS_LOADING;
                        downloadTask.save();
                        return GsonUtils.toJson(downloadTask);
                    }
                }
                // ?????????,?????????
            } else {
                return GsonUtils.toJson(downloadTask);
            }
        }
        return null;
    }


    @JavascriptInterface
    public void installApp(String url) {
        List<DownloadTask> downloadTasks = DownloadTask.find(DownloadTask.class, "download_url=?", url);
        if (downloadTasks.size() != 0) {
            DownloadTask downloadTask = downloadTasks.get(0);
            if (FileUtils.isFileExists(downloadTask.getLocalPath())) {
                CommonUtils.installApk(downloadTask.getLocalPath());
                if (mReceiver != null) {
                    mActivity.unregisterReceiver(mReceiver);
                }
                mReceiver = AppInstalledReceiverKt.regisBroadcastReceiver(mActivity, downloadTask, this);
            } else {
                CommonFunctionKt.showToast("Apk????????????????????????,????????????????????????");
            }
        }
    }

    @JavascriptInterface
    public void startApp(String appInfo) {
        if (appInfo.startsWith("http://") || appInfo.startsWith("https://")) {
            List<DownloadTask> downloadTasks = DownloadTask.find(DownloadTask.class, "download_url=?", appInfo);
            if (downloadTasks.size() != 0) {
                DownloadTask downloadTask = downloadTasks.get(0);
                if (CommonUtils.isAppInstalled(downloadTask.getPackageName())) {
                    CommonUtils.startApp(downloadTask.getPackageName());
                } else {
                    CommonFunctionKt.showToast("app?????????");
                }
            }
            return;
        }
        if (CommonUtils.isAppInstalled(appInfo)) {
            CommonUtils.startApp(appInfo);
        } else {
            CommonFunctionKt.showToast("app?????????");
        }
    }

    public void onDestroy() {
        if (mReceiver != null) {
            mActivity.unregisterReceiver(mReceiver);
        }
        mWebView.removeJavascriptInterface(JAVA_IMPL_TARGET_DOWNLOAD);
        mActivity = null;
        if (downloadTask == null) {
            return;
        }
        MultiTaskDownloader.INSTANCE.cancelSingleTaskListener(downloadTask.getId());
    }

    @Override
    public void appInstallSuccess(@NonNull DownloadTask task) {
        mReceiver = null;
        task.installed = DOWNLOAD_STATUS_INSTALLED;
        task.save();
        updateTaskProgress(task);
    }

    @Override
    public void updateTaskProgress(@NonNull DownloadTask task) {
        mWebView.loadUrl(String.format("javascript:callDownloadTaskProgress(%s,%s,%s)", task.getUrl(), task.getState(), task.getProgress()));
    }
}
