package com.duoyou.develop.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.duoyou.develop.ui.WebViewActivity;
import com.duoyou.develop.view.ToastHelper;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static android.os.Environment.DIRECTORY_DCIM;

public class WebViewUtils {

    public static void initWebViewSettings(Activity activity, WebView webView) {
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        String appCachePath = webView.getContext().getCacheDir().getAbsolutePath();
        webSetting.setAppCachePath(appCachePath);
        webSetting.setDatabasePath(appCachePath);
        webSetting.setSavePassword(true);
        webSetting.setPluginsPath(appCachePath);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(1024 * 1024 * 1024);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setUserAgentString(webView.getSettings().getUserAgentString().concat(";duoyou_duoduomanghe_h5"));
        if (AppInfoUtils.isDebug()) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        CookieSyncManager.createInstance(webView.getContext());
        CookieSyncManager.getInstance().sync();
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        setDownoadListener(activity, webView);

        if (activity instanceof WebViewActivity) {
//            webView.setWebChromeClient(((WebViewActivity) activity).mOpenFileWebChromeClient);
        }
        setLongClickListener(webView);
    }

    private static void setLongClickListener(WebView webView) {
        webView.setOnLongClickListener(v -> {
            final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
            // 如果是图片类型或者是带有图片链接的类型
            if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                    hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                // 弹出保存图片的对话框
                new AlertDialog.Builder(webView.getContext())
                        .setItems(new String[]{"保存图片到本地"}, (dialog, which) -> {
                            String pic = hitTestResult.getExtra();//获取图片
                            switch (which) {
                                case 0:
                                    //保存图片到相册
//                                    saveImage(pic);
//                                    ImageSaveUtil.glideSaveImage(webView.getContext(), pic);
//                                    new Thread(() -> saveImage(pic)).start();
                                    break;
//                                case 1:
//                                    // 分享图片，这里用RxJava处理异步
//                                    Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> subscriber.onNext(webData2bitmap(pic)))
//                                            .subscribeOn(Schedulers.computation())
//                                            .observeOn(AndroidSchedulers.mainThread())
//                                            .subscribe(bitmap -> {
//                                                try {
//                                                    Intent intent = new Intent(Intent.ACTION_SEND);
//                                                    intent.setType("image/*");
//                                                    intent.putExtra(Intent.EXTRA_STREAM, getImageUri(WebActivity.this, bitmap));
//                                                    startActivity(Intent.createChooser(intent, "分享图片"));
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                    Util.makeText(WebActivity.this, "分享失败");
//                                                }
//                                            }, throwable -> {
//                                                throwable.printStackTrace();
//                                                Util.makeText(WebActivity.this, "分享失败");
//                                            });
//                                    break;
                            }
                        })
                        .show();
                return true;
            }
            return false;//保持长按可以复制文字
        });
    }

    public static void saveImage(String data) {
        try {
            Bitmap bitmap = webData2bitmap(data);
            if (bitmap != null) {
                save2Album(bitmap, "zhiban_" + TimeUtils.getNowMills() + ".jpg");
            } else {
                ToastHelper.showShort("保存失败");
            }
        } catch (Exception e) {
            ToastHelper.showShort("保存失败");
            e.printStackTrace();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap webData2bitmap(String data) {
        byte[] imageBytes = Base64.decode(data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private static void save2Album(Bitmap bitmap, String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Utils.getApp().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            ToastHelper.showShort("保存成功");
        } catch (Exception e) {
            ToastHelper.showShort("保存失败");
            e.printStackTrace();
        }
    }

    public static void setDownoadListener(final Activity activity, WebView webView) {
//        webView.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            final DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
//                            //以下两行代码可以让下载的apk文件被直接安装而不用使用Fileprovider,系统7.0或者以上才启动。
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                StrictMode.VmPolicy.Builder localBuilder = new StrictMode.VmPolicy.Builder();
//                                StrictMode.setVmPolicy(localBuilder.build());
//                            }
//                            final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                            // 通知栏中将出现的内容
//
//                            //7.0以上的系统适配
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                request.setRequiresDeviceIdle(false);
//                                request.setRequiresCharging(false);
//                            }
//
//                            // 下载过程和下载完成后通知栏有通知消息。
//                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
//                            String fileName = System.currentTimeMillis() + "";
//
//                            try {
//                                fileName = url.substring(url.lastIndexOf("/") + 1);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            PathUtils.SYSTEM_DOWNLOAD_FILEPATH = PathUtils.getDownloadFilePath() + fileName;
//
//                            File file = new File(PathUtils.SYSTEM_DOWNLOAD_FILEPATH);
//
//                            if (CommonUtils.isApkOk(PathUtils.SYSTEM_DOWNLOAD_FILEPATH)) {
//                                CommonUtils.installApk(PathUtils.SYSTEM_DOWNLOAD_FILEPATH);
//                                return;
//                            }
//
//                            if (file.exists()) {
//                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
//                                alertDialog.setTitle("温馨提示");
//                                alertDialog.setMessage("当前文件已经存在，是否重新下载？");
//                                alertDialog.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // 指定下载文件地址，使用这个指定地址可不需要WRITE_EXTERNAL_STORAGE权限。
//                                        request.setDestinationUri(Uri.fromFile(new File(PathUtils.SYSTEM_DOWNLOAD_FILEPATH)));
//                                        //大于11版本手机允许扫描
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { //表示允许MediaScanner扫描到这个文件，默认不允许。
//                                            request.allowScanningByMediaScanner();
//                                        }
//                                        downloadManager.enqueue(request);
//                                        ToastHelper.showShort("当前任务正在下载中，可在通知栏查看");
//                                    }
//                                });
//
//                                alertDialog.setNegativeButton("打开该文件", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        try {
//                                            Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            activity.startActivity(intent);
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                                alertDialog.show();
//                            } else {
//                                // 指定下载文件地址，使用这个指定地址可不需要WRITE_EXTERNAL_STORAGE权限。
//                                request.setDestinationUri(Uri.fromFile(new File(PathUtils.SYSTEM_DOWNLOAD_FILEPATH)));
//                                //大于11版本手机允许扫描
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { //表示允许MediaScanner扫描到这个文件，默认不允许。
//                                    request.allowScanningByMediaScanner();
//                                }
//                                downloadManager.enqueue(request);
//                                ToastHelper.showShort("当前任务正在下载中，可在通知栏查看");
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            CommonUtils.openBrowser(activity, url);
//                        }
//                    }
//                });
//
//            }
//        });
    }

    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

}
