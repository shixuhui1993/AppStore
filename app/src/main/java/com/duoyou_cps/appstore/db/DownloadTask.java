package com.duoyou_cps.appstore.db;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import io.reactivex.rxjava3.disposables.Disposable;

@Table(name = "DownloadTask")
public class DownloadTask extends SugarRecord {
    @Unique
    @Column(name = "download_url")
    private String url;
    @Column(name = "local_path")
    private String localPath;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "package_name")
    private String packageName;

    @Column(name = "current_size")
    private long currentSize;
    @Column(name = "total_size")
    private long totalSize;
    @Column(name = "state")
    private int state; //0=未开始 1=等待中
    @Column(name = "file_type")
    private int fileType; // 文件类型
    @Column(name = "extra1")
    private String extra1;
    @Column(name = "extra2")
    private String extra2;
    @Column(name = "other_info")
    private String otherInfo;
    @Column(name = "installed")
    public int installed;
    @Ignore
    public Disposable mDisposable;
    @Column(name = "progress")
    private int progress;
    @Ignore
    public boolean showDelete;

    public DownloadTask() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public DownloadTask(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Disposable getDisposable() {
        return mDisposable;
    }

    public void setDisposable(Disposable disposable) {
        mDisposable = disposable;
    }

    public boolean isDownloading() {
        return mDisposable != null && !mDisposable.isDisposed();
    }
}
